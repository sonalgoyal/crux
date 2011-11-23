/**
 * Copyright 2011 Nube Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package co.nubetech.crux.server.filter;

import java.util.HashMap;

import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;

import co.nubetech.crux.server.filter.types.BooleanComparator;
import co.nubetech.crux.server.filter.types.DoubleComparator;
import co.nubetech.crux.server.filter.types.FloatComparator;
import co.nubetech.crux.server.filter.types.IntComparator;
import co.nubetech.crux.server.filter.types.LongComparator;
import co.nubetech.crux.server.filter.types.ShortComparator;

public class ComparatorRegister {

	public static final HashMap<String, Class<? extends WritableByteArrayComparable>> comparators = new HashMap<String, Class<? extends WritableByteArrayComparable>>();

	static {
		comparators.put("java.lang.Long", LongComparator.class);
		comparators.put("java.lang.Double", DoubleComparator.class);
		comparators.put("java.lang.Float", FloatComparator.class);
		comparators.put("java.lang.Int", IntComparator.class);
		comparators.put("java.lang.Short", ShortComparator.class);
		comparators.put("java.lang.Boolean", BooleanComparator.class);
		comparators.put("java.lang.String", BooleanComparator.class);
	}

	public static Class<? extends WritableByteArrayComparable> getComparator(
			String className) throws ComparatorNotFoundException {
		if (comparators.containsKey(className)) {
			return comparators.get(className);
		} else
			throw new ComparatorNotFoundException(
					"There is no comparator registered for the class: "
							+ className);

	}

}
