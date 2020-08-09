package org.bboss.elasticsearchtest.apollo;
/**
 * Copyright 2020 bboss
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

import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.frameworkset.apollo.PropertiesChangeListener;
import org.frameworkset.elasticsearch.client.HostDiscoverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.TreeSet;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/8/9 23:10
 * @author biaoping.yin
 * @version 1.0
 */
public class NodeChangeListener extends PropertiesChangeListener {
	private Set<String> elasticsearchPools;
	private static Logger logger = LoggerFactory.getLogger(NodeChangeListener.class);
	String defaultHostsKey = "elasticsearch.rest.hostNames";
	private void handleDiscoverHosts(String _hosts,String poolName){
		if(_hosts != null && !_hosts.equals("")){
			String[] hosts = _hosts.split(",");

			//将被动获取到的地址清单加入服务地址组poolName中
			HostDiscoverUtil.handleDiscoverHosts(hosts,poolName);
		}
	}
	private void poolChange(ConfigChangeEvent changeEvent ,String pool){
		Set<String> changedKeys = changeEvent.changedKeys();
		ConfigChange hostsChange = null;
		boolean isdefault = pool == null || pool.equals("default");
		String hostsKey = null;

		if(!isdefault){
			hostsKey = pool+".elasticsearch.rest.hostNames";
		}
		else{
			hostsKey = "default.elasticsearch.rest.hostNames";
		}

		for (String key : changedKeys) {
			if(key.equals(hostsKey) || (isdefault && key.equals(defaultHostsKey))){//schedule集群
				hostsChange = changeEvent.getChange(key);


			}



		}
		if(hostsChange != null && hostsChange.getChangeType() == PropertyChangeType.MODIFIED){
			logger.info("Found change - key: {}, oldValue: {}, newValue: {}, changeType: {}",
					hostsChange.getPropertyName(), hostsChange.getOldValue(),
					hostsChange.getNewValue(), hostsChange.getChangeType());

			String _hosts = hostsChange.getNewValue();
			//连通host和rounting一同更新
			handleDiscoverHosts(_hosts, pool);

		}

	}
	/**
	 * //模拟被动获取监听地址清单
	 * List<HttpHost> hosts = new ArrayList<HttpHost>();
	 * // https服务必须带https://协议头,例如https://192.168.137.1:808
	 * HttpHost host = new HttpHost("192.168.137.1:808");
	 * hosts.add(host);
	 *
	 *    host = new HttpHost("192.168.137.1:809");
	 *    hosts.add(host);
	 *
	 * host = new HttpHost("192.168.137.1:810");
	 * hosts.add(host);
	 * //将被动获取到的地址清单加入服务地址组report中
	 * HttpProxyUtil.handleDiscoverHosts("schedule",hosts);
	 */
	public void onChange(ConfigChangeEvent changeEvent) {
		if(logger.isInfoEnabled()) {
			logger.info("Changes for namespace {}", changeEvent.getNamespace());
		}
		if(elasticsearchPools == null || elasticsearchPools.size() == 0){
			logger.info("Changes for elasticsearch hosts ignored: elasticsearchPools is not setted yet.");
			return;
		}
		for(String pool:elasticsearchPools) {
			poolChange(changeEvent, pool);
		}

	}




	@Override
	public void completeLoaded() {
		elasticsearchPools =  new TreeSet<String>();
		if(this.propertiesContainer != null){
			String poolNames = propertiesContainer.getProperty("elasticsearch.serverNames");
			if(poolNames != null){

				String tmp[] = poolNames.split(",");
				for(int i = 0; i < tmp.length; i ++){
					this.elasticsearchPools.add(tmp[i].trim());
				}
			}
			else{
				this.elasticsearchPools.add("default");
			}
		}
	}
}
