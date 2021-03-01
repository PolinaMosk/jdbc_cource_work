package controllers.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableUtil {

    public void initRows(ResultSet rs, TableView<ObservableList> table) throws SQLException {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        while(rs.next()){
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                row.add(rs.getString(i));
            }
            data.add(row);

        }
        table.setItems(data);
    }

    public void init_columns(ResultSet rs, TableView<ObservableList> table) throws SQLException {
        for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
            final int j = i;
            TableColumn col = table.getColumns().get(i);
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> {
                if (param.getValue().get(j) == null) return new SimpleStringProperty("-");
                else return new SimpleStringProperty(param.getValue().get(j).toString());
            });
        }
        initRows(rs, table);
    }
}
