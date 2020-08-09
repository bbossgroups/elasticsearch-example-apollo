package org.bboss.elasticsearchtest.index;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.junit.Test;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/7/25 22:53
 * @author biaoping.yin
 * @version 1.0
 */
public class ReindexTest {
	@Test
	public void reIndex(){
		ClientInterface clientInterface = ElasticSearchHelper.getRestClientUtil();
		String response  = clientInterface.reindex("demo","newdemo");
		System.out.println(response);
		response = clientInterface.getIndexMapping("newdemo",true);
		System.out.println(response);
		long count  = clientInterface.countAll("newdemo");
		System.out.println(count);
	}

}
