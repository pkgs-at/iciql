/*
 * Copyright 2004-2011 H2 Group.
 * Copyright 2011 James Moger.
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

package com.iciql;

class ConditionTrueFalse<A> implements Token {
	A x;
	boolean y;

	ConditionTrueFalse(A x, boolean y) {
		this.x = x;
		this.y = y;
	}

	public <T> void appendSQL(SQLStatement stat, Query<T> query) {
		query.appendSQL(stat, null, x);
		stat.appendSQL(" ");
		stat.appendSQL(CompareType.EQUAL.getString());
		stat.appendSQL(" ");
		query.appendSQL(stat, x, y);
	}
}
