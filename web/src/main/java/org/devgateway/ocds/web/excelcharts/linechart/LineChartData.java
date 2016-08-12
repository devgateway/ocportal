/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.devgateway.ocds.web.excelcharts.linechart;

import org.apache.poi.ss.usermodel.charts.ChartData;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;

import java.util.List;

/**
 * Data for a Line Chart
 */
public interface LineChartData extends ChartData {

    /**
     * @param categories data source for categories.
     * @param values     data source for values.
     * @return a new line chart serie.
     */
    LineChartSeries addSeries(ChartDataSource<?> categories, ChartDataSource<? extends Number> values);

    /**
     * @return list of all series.
     */
    List<? extends LineChartSeries> getSeries();
}
