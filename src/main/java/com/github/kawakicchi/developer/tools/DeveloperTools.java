/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kawakicchi.developer.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author Kawakicchi
 *
 */
public class DeveloperTools {

	public static void main(final String[] args) {

		DeveloperTools tools = new DeveloperTools();
		tools.execute();

	}

	public DeveloperTools() {

	}

	public void execute() {
		System.out.println(read(new File("./src/test/data/sample.sh"), Charset.forName("UTF-8")));
		System.out.println(read(new File("./src/test/data/sample.sql"), Charset.forName("Windows-31J")));
	}

	public String read(final File file, final Charset charset) {
		StringBuilder s = new StringBuilder();
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file), charset);
			char buffer[] = new char[1024];
			int read = -1;
			while (-1 != (read = reader.read(buffer, 0, 1024))) {
				s.append(buffer, 0, read);
			}
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return s.toString();
	}
}
