package cn.wanfeng.sp.bootexecute;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @date: 2024-11-07 21:20
 * @author: luozh.wanfeng
 * @description: 启动初始化OpenSearch数据
 * @since: 1.0
 */
@Component
public class OpenSearchInitExecutor {

    @Resource
    private OpenSearchClient openSearchClient;

    @PostConstruct
    public void initIndex() throws IOException {
        // 索引是否存在,不存在则创建
        BooleanResponse response = openSearchClient.indices().exists(e -> e.index(SimpleProtoConfig.dataTable));
        if (!response.value()) {
            // 创建索引
            openSearchClient.indices().create(c -> c.index(SimpleProtoConfig.dataTable));
            LogUtil.info("初始化创建OpenSearch索引[{}]", SimpleProtoConfig.dataTable);
        }
        LogUtil.info("OpenSearch索引初始化完成");
    }
}
