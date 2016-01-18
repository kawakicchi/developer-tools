package com.github.kawakicchi.developer.svn;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.BeanPropertySetterRule;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreateRule;
import org.apache.commons.digester.SetNextRule;
import org.apache.commons.digester.SetPropertiesRule;
import org.xml.sax.SAXException;

import com.github.kawakicchi.developer.svn.entity.LogEntity;
import com.github.kawakicchi.developer.svn.entity.LogEntryEntity;
import com.github.kawakicchi.developer.svn.entity.LogPathEntity;

public class SVNCommand {

	private File modulePath;

	public SVNCommand(final File module) {
		modulePath = module;
	}

	public LogEntity log(final String path, final String rev) {
		LogEntity result = null;

		List<String> cmds = new ArrayList<String>();
		cmds.add(modulePath.getAbsolutePath());
		cmds.add("log");
		cmds.add("-r");
		cmds.add(rev);
		cmds.add("--xml");
		cmds.add("-v");
		cmds.add("-q");
		cmds.add(path);

		Object obj = execute(cmds, getLogDigester());
		if (null != obj) {
			result = (LogEntity) obj;
		}
		return result;
	}

	public Object execute(final List<String> cmds, final Digester digester) {
		Object result = null;
		
		try {
			ProcessBuilder pb = new ProcessBuilder();
			pb.command(cmds);

			Process process = pb.start();

			XMLParserThread it = new XMLParserThread(digester, process.getInputStream());
			InputStreamThread et = new InputStreamThread(process.getErrorStream());
			it.start();
			et.start();

			process.waitFor();

			it.join();
			et.join();

			if (0 == process.exitValue()) {
				result = it.getParseObject();
			}

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private Digester getLogDigester() {
		Digester digester = new Digester();
		digester.addRule("log", new ObjectCreateRule(LogEntity.class));

		digester.addRule("log/logentry", new ObjectCreateRule(LogEntryEntity.class));
		digester.addRule("log/logentry", new SetNextRule("setLogEntry"));

		digester.addRule("log/logentry/paths/path", new ObjectCreateRule(LogPathEntity.class));
		digester.addRule("log/logentry/paths/path", new SetPropertiesRule("action", "action"));
		digester.addRule("log/logentry/paths/path", new SetPropertiesRule("kind", "kind"));
		digester.addRule("log/logentry/paths/path", new BeanPropertySetterRule("path"));
		digester.addRule("log/logentry/paths/path", new SetNextRule("addPath"));
		return digester;
	}

	/**
	 * InputStreamを読み込むスレッド
	 */
	public static class InputStreamThread extends Thread {

		private BufferedReader reader;

		private List<String> list = new ArrayList<String>();

		/** コンストラクター */
		public InputStreamThread(final InputStream is) {
			reader = new BufferedReader(new InputStreamReader(is));
		}

		/** コンストラクター */
		public InputStreamThread(final InputStream is, final String charset) {
			try {
				reader = new BufferedReader(new InputStreamReader(is, charset));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void run() {
			try {
				for (;;) {
					String line = reader.readLine();
					if (line == null)
						break;
					list.add(line);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/** 文字列取得 */
		public List<String> getStringList() {
			return list;
		}
	}

	/**
	 * InputStreamを読み込むスレッド
	 */
	public static class XMLParserThread extends Thread {

		private Digester digester;
		private InputStream stream;
		private Object object;

		/** コンストラクター */
		public XMLParserThread(final Digester digester, final InputStream is) {
			this.digester = digester;
			stream = is;
		}

		@Override
		public void run() {
			ByteArrayOutputStream out = null;
			try {
				out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int size = -1;
				while (-1 != (size = stream.read(buffer, 0, 1024))) {
					out.write(buffer, 0, size);
				}

				ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
				System.out.println(toString(in, "UTF-8"));

				object = digester.parse(in);
			} catch (SAXException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				if (null != stream) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private String toString(final InputStream stream, final String charset) {
			StringBuffer s = new StringBuffer();

			if (stream.markSupported()) {

				BufferedReader reader = null;
				try {

					reader = new BufferedReader(new InputStreamReader(stream, charset));
					char[] buffer = new char[1024];
					int size = -1;
					while (-1 != (size = reader.read(buffer, 0, 1024))) {
						s.append(buffer, 0, size);
					}

					stream.reset();
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					if (null != reader) {
						try {
							reader.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			} else {
				System.out.println("Unsupported mark.");
			}

			return s.toString();
		}

		public Object getParseObject() {
			return object;
		}
	}
}
