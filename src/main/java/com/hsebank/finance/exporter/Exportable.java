package com.hsebank.finance.exporter;

/**
 * Интерфейс для объектов, поддерживающих экспорт через Visitor
 */
public interface Exportable {
    void accept(DataExportVisitor visitor);
}