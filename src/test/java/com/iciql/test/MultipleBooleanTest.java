/*
 * Copyright (c) 2009-2014, Architector Inc., Japan
 * All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iciql.test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import com.iciql.Iciql;
import com.iciql.Db;

public class MultipleBooleanTest {

	private Db db;

	@Before
	public void setUp() {
		db = IciqlSuite.openNewDb();
		Assume.assumeTrue(IciqlSuite.isH2(db));
		db.executeUpdate(
				"CREATE TABLE t_multiple_boolean(" +
				"boolean_flag BOOLEAN NOT NULL," +
				"primitive_flag BOOLEAN NOT NULL," +
				"object_flag BOOLEAN NULL)");
	}

	@After
	public void tearDown() {
		db.executeUpdate("DROP TABLE t_multiple_boolean");
		db.close();
	}

	// member sealed model sample
	@Iciql.IQTable(
			name = "t_multiple_boolean",
			annotationsOnly = true,
			create = false)
	public static class Model {

		// package scope: visible for data access layer
		@Iciql.IQColumn(name = "boolean_flag")
		boolean booleanFlag;

		@Iciql.IQBoolean
		@Iciql.IQColumn(name = "primitive_flag")
		int primitiveFlag;

		@Iciql.IQBoolean
		@Iciql.IQColumn(name = "object_flag")
		Integer objectFlag;

		public Model(
				boolean booleanFlag,
				int primitiveFlag,
				Integer objectFlag) {
			this.booleanFlag = booleanFlag;
			this.primitiveFlag = primitiveFlag;
			this.objectFlag = objectFlag;
		}

		public Model() {
			this(false, 0, null);
		}

		// public getter
		public boolean isBooleanFlag() {
			return this.booleanFlag;
		}

		// public setter
		public void setBooleanFlag(boolean value) {
			this.booleanFlag = value;
		}

		public boolean isPrimitiveFlag() {
			return this.primitiveFlag != 0;
		}

		public void setPrimitiveFlag(boolean value) {
			this.primitiveFlag = value ? 1 : 0;
		}

		// nullable value (getXxx instead of isXxx)
		public Boolean getObjectFlag() {
			return this.objectFlag == null ? null : (this.objectFlag != 0);
		}

		public void setObjectFlag(Boolean value) {
			this.objectFlag = (value == null ? null : (value ? 1 : 0));
		}

	}

	@Test
	public void test() {
		Model model;

		db.insert(new Model(false, 0, null));
		db.insert(new Model(false, 0, 0));
		db.insert(new Model(false, 0, 1));
		db.insert(new Model(false, 1, null));
		db.insert(new Model(false, 1, 0));
		db.insert(new Model(false, 1, 1));
		db.insert(new Model(true, 0, null));
		db.insert(new Model(true, 0, 0));
		db.insert(new Model(true, 0, 1));
		db.insert(new Model(true, 1, null));
		db.insert(new Model(true, 1, 0));
		db.insert(new Model(true, 1, 1));
		model = new Model();
		assertEquals(
				db.from(model)
						.where(model.booleanFlag).isTrue()
						.selectCount(), 6);
		assertEquals(
				db.from(model)
						.where(model.booleanFlag).isTrue()
						.and(model.primitiveFlag).isTrue()
						.selectCount(), 3);
		assertEquals(
				db.from(model)
						.where(model.booleanFlag).isTrue()
						.and(model.primitiveFlag).isTrue()
						.and(model.objectFlag).isFalse()
						.selectCount(), 1);
	}

}
