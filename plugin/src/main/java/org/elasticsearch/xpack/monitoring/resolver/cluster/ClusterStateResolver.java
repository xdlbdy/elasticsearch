/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.monitoring.resolver.cluster;

import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.set.Sets;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.xpack.monitoring.MonitoredSystem;
import org.elasticsearch.xpack.monitoring.collector.cluster.ClusterStateMonitoringDoc;
import org.elasticsearch.xpack.monitoring.resolver.MonitoringIndexNameResolver;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class ClusterStateResolver extends MonitoringIndexNameResolver.Timestamped<ClusterStateMonitoringDoc> {

    public static final String TYPE = "cluster_state";

    static final Set<String> FILTERS;
    static {
        Set<String> filters = Sets.newHashSet(
                "cluster_uuid",
                "timestamp",
                "type",
                "source_node",
                "cluster_state.version",
                "cluster_state.master_node",
                "cluster_state.state_uuid",
                "cluster_state.status",
                "cluster_state.nodes");
        FILTERS = Collections.unmodifiableSet(filters);
    }

    public ClusterStateResolver(MonitoredSystem id, Settings settings) {
        super(id, settings);
    }

    @Override
    public Set<String> filters() {
        return FILTERS;
    }

    @Override
    protected void buildXContent(ClusterStateMonitoringDoc document, XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject(Fields.CLUSTER_STATE);
        ClusterState clusterState = document.getClusterState();
        if (clusterState != null) {
            builder.field(Fields.STATUS, document.getStatus().name().toLowerCase(Locale.ROOT));
            clusterState.toXContent(builder, params);
        }
        builder.endObject();
    }

    static final class Fields {
        static final String CLUSTER_STATE = TYPE;
        static final String STATUS = "status";
    }
}
