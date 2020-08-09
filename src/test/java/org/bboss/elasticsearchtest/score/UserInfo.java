package org.bboss.elasticsearchtest.score;
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

import com.frameworkset.orm.annotation.ESId;
import org.frameworkset.elasticsearch.entity.ESBaseData;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/3/16 14:19
 * @author biaoping.yin
 * @version 1.0
 */
public class UserInfo extends ESBaseData {
	@ESId(readSet = true)
	private String userId;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String toString(){
		StringBuilder ret = new StringBuilder();
		ret.append("{\"name\":").append("\"").append(this.getName()).append("\",");
		ret.append("\"userId\":").append("\"").append(this.getUserId()).append("\",");
		ret.append("\"score\":").append(this.getScore()).append("}");
		ret.append("\n");
		return ret.toString();
	}
}
