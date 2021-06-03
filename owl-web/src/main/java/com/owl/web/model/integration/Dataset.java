package com.owl.web.model.integration;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Dataset {
    private List<Column> columns;
    private List<Row> rows;
}
