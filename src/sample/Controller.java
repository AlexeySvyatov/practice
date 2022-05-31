package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
    @FXML private Text dateFromTxt;
    @FXML private Text dateToTxt;
    @FXML private Text workMarkTxt;
    @FXML private Button saveBtn;
    @FXML private Button chooseBtn;
    @FXML private Button docsBtn;
    @FXML private Button socialsBtn;
    @FXML private Button employmentBtn;
    @FXML private Button requestBtn;
    @FXML private Button exitBtn;
    @FXML private TextField organization;
    @FXML private TextField profession;
    @FXML private TextField workMark;
    @FXML private DatePicker dateTo;
    @FXML private DatePicker dateFrom;
    @FXML private RadioButton noteduRBtn;
    @FXML private RadioButton eduRBtn;
    @FXML private ComboBox professionBox;
    @FXML private ComboBox organizationBox;
    @FXML private TableView<ObservableList> requestTable;

    private ObservableList<String> profs = FXCollections.observableArrayList();
    private ObservableList<String> orgs = FXCollections.observableArrayList();

    DBHandler dbHandler = new DBHandler();

    @FXML
    private void initialize(){
        ToggleGroup group = new ToggleGroup();
        eduRBtn.setToggleGroup(group);
        noteduRBtn.setToggleGroup(group);
        eduRBtn.setSelected(true);

        initOrganizations();
        initProfessions();

        eduRBtn.setOnAction(event -> {
            organizationBox.setVisible(true);
            professionBox.setVisible(true);
            profession.setVisible(false);
            organization.setVisible(false);
        });
        noteduRBtn.setOnAction(event -> {
            organizationBox.setVisible(false);
            professionBox.setVisible(false);
            profession.setVisible(true);
            organization.setVisible(true);
        });

        saveBtn.setOnAction(event -> {
            if(eduRBtn.isSelected()){
                if(!(organizationBox.getValue() == null || professionBox.getValue() == null || dateTo.getValue() == null
                || dateFrom.getValue() == null || workMark.getText().isEmpty())){
                    try{
                        PreparedStatement statement = dbHandler.getDbConnection().prepareStatement("INSERT into labor_book(id_lb,id_employee,id_organization,id_profession,work_mark,date_from,date_to) VALUES(?,?,?,?,?,?,?)");
                        statement.setInt(1, 1);
                        statement.setInt(2, 1 );
                        statement.setInt(3, 1);
                        statement.setInt(4, 5);
                        statement.setString(5,workMark.getText());
                        statement.setDate(6, Date.valueOf(dateFrom.getValue()));
                        statement.setDate(7, Date.valueOf(dateTo.getValue()));
                        statement.executeUpdate();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }else if(noteduRBtn.isSelected()){
                if(!(dateTo.getValue() == null || dateFrom.getValue() == null || workMark.getText().isEmpty()
                        || organization.getText().isEmpty() || profession.getText().isEmpty())){
                    try{
                        PreparedStatement statement = dbHandler.getDbConnection().prepareStatement("INSERT into labor_book(id_lb,id_employee,not_edu_organization,not_edu_profession,work_mark,date_from,date_to) VALUES(?,?,?,?,?,?,?)");
                        statement.setInt(1, 2);
                        statement.setInt(2, 3);
                        statement.setString(3, organization.getText());
                        statement.setString(4, profession.getText());
                        statement.setString(5,workMark.getText());
                        statement.setDate(6, Date.valueOf(dateFrom.getValue()));
                        statement.setDate(7, Date.valueOf(dateTo.getValue()));
                        statement.executeUpdate();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        docsBtn.setOnAction(event -> {
            dateFromTxt.setVisible(false);
            dateToTxt.setVisible(false);
            workMarkTxt.setVisible(false);
            saveBtn.setVisible(false);
            organization.setVisible(false);
            profession.setVisible(false);
            workMark.setVisible(false);
            dateFrom.setVisible(false);
            dateTo.setVisible(false);
            eduRBtn.setVisible(false);
            noteduRBtn.setVisible(false);
            organizationBox.setVisible(false);
            professionBox.setVisible(false);
        });
        socialsBtn.setOnAction(event -> {
            dateFromTxt.setVisible(false);
            dateToTxt.setVisible(false);
            workMarkTxt.setVisible(false);
            saveBtn.setVisible(false);
            organization.setVisible(false);
            profession.setVisible(false);
            workMark.setVisible(false);
            dateFrom.setVisible(false);
            dateTo.setVisible(false);
            eduRBtn.setVisible(false);
            noteduRBtn.setVisible(false);
            organizationBox.setVisible(false);
            professionBox.setVisible(false);
        });
        employmentBtn.setOnAction(event -> {
            chooseBtn.setVisible(false);
            requestTable.setVisible(false);
            dateFromTxt.setVisible(true);
            dateToTxt.setVisible(true);
            workMarkTxt.setVisible(true);
            saveBtn.setVisible(true);
            organization.setVisible(true);
            profession.setVisible(true);
            workMark.setVisible(true);
            dateFrom.setVisible(true);
            dateTo.setVisible(true);
            eduRBtn.setVisible(true);
            noteduRBtn.setVisible(true);
            organizationBox.setVisible(true);
            professionBox.setVisible(true);
        });
        requestBtn.setOnAction(event -> {
            dateFromTxt.setVisible(false);
            dateToTxt.setVisible(false);
            workMarkTxt.setVisible(false);
            saveBtn.setVisible(false);
            organization.setVisible(false);
            profession.setVisible(false);
            workMark.setVisible(false);
            dateFrom.setVisible(false);
            dateTo.setVisible(false);
            eduRBtn.setVisible(false);
            noteduRBtn.setVisible(false);
            organizationBox.setVisible(false);
            professionBox.setVisible(false);
            chooseBtn.setVisible(true);
            requestTable.setVisible(true);

            String request = "SELECT id_personal as 'ID', (select organization.short_name from organization where personnel.id_organization = organization.id_organization) as 'Организация', (select name_profession from practice.professions WHERE personnel.id_profession = professions.id_profession) as 'Профессия', (select (select name_subject from subjects where professions.id_subject = subjects.id_subject) FROM professions WHERE personnel.id_profession = professions.id_profession) as 'Предмет', date_from as 'Дата создания' from personnel where personnel.job_status = 0;";
            try {
                fill(request, requestTable);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            chooseBtn.setOnAction(actionEvent -> {
                ObservableList selectId = requestTable.getSelectionModel().getSelectedItem();
                Object selectIndex = selectId.get(0);
                String upRequest = "UPDATE personnel SET job_status = 1 WHERE(id_personal = " + selectIndex + ")";
                try {
                    PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(upRequest);
                    prSt.executeUpdate();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                requestTable.getItems().clear();
                String requestPersonnel = "SELECT id_personal as 'ID', (select organization.short_name from organization where personnel.id_organization = organization.id_organization) as 'Организация', (select name_profession from practice.professions WHERE personnel.id_profession = professions.id_profession) as 'Профессия', (select (select name_subject from subjects where professions.id_subject = subjects.id_subject) FROM professions WHERE personnel.id_profession = professions.id_profession) as 'Предмет', date_from as 'Дата создания' from personnel where personnel.job_status = 0;";
                try {
                    fill(requestPersonnel, requestTable);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        });
    }
    private void initProfessions(){
        try{
            dbHandler.dbConnection = dbHandler.getDbConnection();
            ResultSet resSet = dbHandler.dbConnection.createStatement().executeQuery("SELECT name_profession FROM `professions`");
            while (resSet.next()) {
                profs.add(resSet.getString("name_profession"));
            }
            professionBox.getItems().addAll(profs);
        } catch (SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }
    private void initOrganizations(){
        try{
            dbHandler.dbConnection = dbHandler.getDbConnection();
            ResultSet resSet = dbHandler.dbConnection.createStatement().executeQuery("SELECT short_name FROM `organization`");
            while (resSet.next()) {
                orgs.add(resSet.getString("short_name"));
            }
            organizationBox.getItems().addAll(orgs);
        } catch (SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public static void fill(String querry, TableView<ObservableList> personalDataTable1) throws SQLException {
        personalDataTable1.getColumns().clear();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        DBHandler dbHandler = new DBHandler();
        ResultSet resultSet = dbHandler.querry(querry);
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnLabel(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if(param.getValue().get(j) == null){
                        return new SimpleStringProperty("");
                    } else {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                }
            });
            personalDataTable1.getColumns().addAll(col);
        }
        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.add(resultSet.getString(i));
            }
            data.add(row);
        }
        personalDataTable1.setItems(data);
    }
}