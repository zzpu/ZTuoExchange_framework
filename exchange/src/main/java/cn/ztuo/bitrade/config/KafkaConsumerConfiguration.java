package cn.ztuo.bitrade.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//这里创建了对应类型的bean之后，org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration中的对应Bean定义将不起作用。
@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

	@Value("${spring.kafka.bootstrap-servers}")
	private String servers;
	@Value("${spring.kafka.consumer.enable.auto.commit}")
	private boolean enableAutoCommit;
	@Value("${spring.kafka.consumer.session.timeout}")
	private String sessionTimeout;
	@Value("${spring.kafka.consumer.auto.commit.interval}")
	private String autoCommitInterval;
	@Value("${spring.kafka.consumer.group.id}")
	private String groupId;
	@Value("${spring.kafka.consumer.auto.offset.reset}")
	private String autoOffsetReset;
	@Value("${spring.kafka.consumer.concurrency}")
	private int concurrency;
	@Value("${spring.kafka.consumer.maxPollRecordsConfig}")
	private int maxPollRecordsConfig;
	//构造消费者属性map，ConsumerConfig中的可配置属性比spring boot自动配置要多
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> propsMap = new HashMap<>();
		propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
		propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
		propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
		propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecordsConfig);// 每个批次获取数
		return propsMap;
	}
	/**
	 * 不使用spring boot默认方式创建的DefaultKafkaConsumerFactory，重新定义创建方式
	 * @return
	 */
	public ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(concurrency);
		factory.setMissingTopicsFatal(false);
		factory.getContainerProperties().setPollTimeout(1500);
		factory.setBatchListener(true);
		return factory;
	}

}
