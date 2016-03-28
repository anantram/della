/*
 *This is della project
 *Idea taken from actual della created at CMU by Lynn Robert Carter.
 *Modified by using javaFX design style.
 *Updated by R A RAM KUMAR and K JYOTIRADITYA
 */




package application;
import java.lang.Object;
import static java.lang.System.exit;
import javafx.util.Duration;
import java.util.*;
import javafx.animation.Animation;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import com.sun.prism.paint.Paint;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class dellaController implements Initializable {

	//defining all the components in the application
	
	@FXML
	private TextField nameInput,newTeamName,actionInput,dueDate,actionNameConsole;
	@FXML
	private Label connectionStatus;
	@FXML
	private ListView membersList, teamsList,currentTeams, availableTeams, currentMembersInTeam,availableMemberForTeam,
						actionsList;
	@FXML
	private Button removeFromList, addToList, addToListTeam, removeFromListTeam, addToTeam, removeFromTeam,addMemberToTeam,
					removeMemberFromTeam, createNewAction, updateAction, clearAction,deleteAction;
	@ FXML
	private Circle circleDesign,circleDesign1,circleDesign2,circleDesign3;
	@FXML
	private ComboBox actionsCombo, membersCombo, actionStatusCombo, teamsCombo;
	@FXML
	private TextArea actionDescription,actionResolution,actionDesConsole,actionResConsole;
	@FXML
	private Label creationLabel,dueConsole, statusConsole, memConsole, teamConsole,creationConsole;
	
	
	public dbConnector conn = new dbConnector(); // Connection to DB
	public boolean isOnline;
	
	@FXML
	private void exitProgram() {
		exit(0);
	}
	
	
	/* 
	 * Functions present in action Items tab
	 * 
	 */
	@FXML
	private void getAction(ActionEvent event) {
		updateActions();
		String selectedAction = (String)actionsCombo.getSelectionModel().getSelectedItem();
		System.out.println(selectedAction);
		try {
			System.out.println("select * from actions where actionname = '" + selectedAction + "'");
			ResultSet allMembers = conn.myStmt.executeQuery("select * from actions where actionname = '" + selectedAction + "'");
			String selectedMember = null, selectedTeam = null;
			while (allMembers.next()) {
				String name = allMembers.getString("actionname");
				String des = allMembers.getString("actiondescription");
				actionInput.setText(name);
				actionDescription.setText(des);
				actionResolution.setText(allMembers.getString("actionResolution"));
				creationLabel.setText((String)allMembers.getString("creationdate"));
				dueDate.setText((String)allMembers.getString("duedate"));
				selectedMember = (String)allMembers.getString("assimem");
				selectedTeam = (String)allMembers.getString("assiteam");
				ObservableList<Object> status = FXCollections.observableArrayList();
				status.add("Open");
				status.add("Close");
				actionStatusCombo.setItems(status);
				actionStatusCombo.setValue((String)allMembers.getString("status"));
				
			}
			if(selectedMember.equals("---No member Selected---")){
				allMembers = conn.myStmt.executeQuery("select teamname from teamnames");
				ObservableList<Object> membersLists = FXCollections.observableArrayList();
				membersLists.add("---No team Selected---");
				
				while (allMembers.next()) {
					membersLists.add(allMembers.getString("teamname"));
				
				}
				teamsCombo.setItems(membersLists);
			}
			
			else {
				allMembers = conn.myStmt.executeQuery("select teamf from empteam where empf = '" + selectedMember +"'");
				ObservableList<Object> membersLists = FXCollections.observableArrayList();
				membersLists.add("---No team Selected---");
				while (allMembers.next()) {
					membersLists.add(allMembers.getString("teamf"));
				
				}
				teamsCombo.setItems(membersLists);
			}
			membersCombo.setValue(selectedMember);
			teamsCombo.setValue(selectedTeam);
			if(isOnline) {
				clearAction.setDisable(false);
				deleteAction.setDisable(false);	
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	@ FXML
	private void clearForm() {
		actionInput.setText("");
		actionDescription.setText("");
		actionResolution.setText("");
		creationLabel.setText("");
		dueDate.setText("");
		membersCombo.setValue("");
		teamsCombo.setValue("");
		clearAction.setDisable(true);
		createNewAction.setDisable(true);
		updateAction.setDisable(true);
	}
	
	
	@FXML
	private void teamSelect() {
		try {
			String selectedTeam = (String)teamsCombo.getSelectionModel().getSelectedItem();
			if(selectedTeam.equals("---No team Selected---")){
				ResultSet allMembers = conn.myStmt.executeQuery("select empname from empnames");
				ObservableList<Object> membersLists = FXCollections.observableArrayList();
				membersLists.add("---No member Selected---");
				while (allMembers.next()) {
					membersLists.add(allMembers.getString("empname"));
				
				}
				membersCombo.setItems(membersLists);
			}
			
			else {
				ResultSet allMembers = conn.myStmt.executeQuery("select empf from empteam where teamf = '" + selectedTeam +"'");
				ObservableList<Object> membersLists = FXCollections.observableArrayList();
				membersLists.add("---No member Selected---");
				while (allMembers.next()) {
					membersLists.add(allMembers.getString("empf"));
				}
				membersCombo.setItems(membersLists);
			}
		}
		catch(Exception e) {
				
		}
	}
	
	
	@FXML
	private void memberSelect() {
		try {
			String selectedMember = (String)membersCombo.getSelectionModel().getSelectedItem();
			if(selectedMember.equals("---No member Selected---")){
				ResultSet allMembers = conn.myStmt.executeQuery("select teamname from teamnames");
				ObservableList<Object> membersLists = FXCollections.observableArrayList();
				membersLists.add("---No team Selected---");
				while (allMembers.next()) {
					membersLists.add(allMembers.getString("teamname"));
				
				}
				teamsCombo.setItems(membersLists);
			}
			
			else {
				ResultSet allMembers = conn.myStmt.executeQuery("select teamf from empteam where empf = '" + selectedMember +"'");
				ObservableList<Object> membersLists = FXCollections.observableArrayList();
				membersLists.add("---No team Selected---");
				while (allMembers.next()) {
					membersLists.add(allMembers.getString("teamf"));
				
				}
				teamsCombo.setItems(membersLists);
			}
		}
		catch(Exception e) {
				
		}
	}
	
	
	
	@FXML
	private void editActionValues() {
		createNewAction.setDisable(true);
		updateAction.setDisable(true);
		clearAction.setDisable(true);
		String actName = actionInput.getText();
		String due = dueDate.getText();
		if(!(actName.equals("") || due.equals(""))) {
			if (actName.equals((String)actionsCombo.getSelectionModel().getSelectedItem())) {
				if(isOnline) {
					updateAction.setDisable(false);
				}
			}
			else 
				if(isOnline) {
					createNewAction.setDisable(false);
				}
		}
		
		if(isOnline) {
			clearAction.setDisable(false);
		}
	}
	
	@FXML
	private void addAction(ActionEvent event) {
		try{
			String actName = actionInput.getText();
			String actDes = actionDescription.getText();
			String actRes = actionResolution.getText();
			String due = dueDate.getText();
			String actMem = (String) membersCombo.getSelectionModel().getSelectedItem();
			String actTeam = (String) teamsCombo.getSelectionModel().getSelectedItem();
			String actStatus = (String) actionStatusCombo.getSelectionModel().getSelectedItem();
			String sql = "insert into actions (actionname,actiondescription,actionresolution,duedate,status,assimem,assiteam,creationdate) values('"
					+ actName +"','" + actDes +"','" + actRes +"','" + due +"','" + actStatus +"','" + actMem +"','"
					+ actTeam +"'," + "curDate()" + ")";
			System.out.println(sql);
	        conn.myStmt.executeUpdate(sql);		
		}
		catch(Exception e) {
			
		}
		createNewAction.setDisable(true);
		updateActions();
		updateConsole();
	}
	@FXML
	private void removeAction(ActionEvent event) {
		try {
	        String sql = "delete from actions where actionname ='" + (String)actionsCombo.getSelectionModel().getSelectedItem() + "'";
	        conn.myStmt.executeUpdate(sql);
	        updateActions();
	        updateConsole();
	        System.out.println(sql);
	          
		}catch(Exception e) {
            System.out.println(e);
        }
		deleteAction.setDisable(true);
	}
	
	@FXML
	private void updateAction() {
		try{
			String actName = actionInput.getText();
			String actDes = actionDescription.getText();
			String actRes = actionResolution.getText();
			String due = dueDate.getText();
			String actMem = (String) membersCombo.getSelectionModel().getSelectedItem();
			String actTeam = (String) teamsCombo.getSelectionModel().getSelectedItem();
			String actStatus = (String) actionStatusCombo.getSelectionModel().getSelectedItem();
			
			String sql = "update actions set actiondescription = '" + actDes + 
					"',actionresolution = '" + actRes + "',duedate = '" + due + "',status = '" + actStatus +
					"',assimem = '" + actMem + "',assiteam = '" + actTeam + "',creationdate = curdate() where actionname = '" + actName + "'"; 

			System.out.println(sql);
	        conn.myStmt.executeUpdate(sql);		
		}
		catch(Exception e) {
			
		}
		createNewAction.setDisable(true);
		updateAction.setDisable(true);
		clearAction.setDisable(true);
		updateActions();
		updateConsole();
	}
	
	/* 
	 * Functions present in console tab
	 * 
	 */
	
	@FXML
	private void selectAction() {
		updateActions();
		String selectedAction = (String)actionsList.getSelectionModel().getSelectedItem();
		System.out.println(selectedAction);
		try {
			System.out.println("select * from actions where actionname = '" + selectedAction + "'");
			ResultSet allMembers = conn.myStmt.executeQuery("select * from actions where actionname = '" + selectedAction + "'");
			while (allMembers.next()) {
				String name = allMembers.getString("actionname");
				String des = allMembers.getString("actiondescription");
				actionNameConsole.setText(name);
				actionDesConsole.setText(des);
				actionResConsole.setText(allMembers.getString("actionResolution"));
				creationConsole.setText((String)allMembers.getString("creationdate"));
				dueConsole.setText((String)allMembers.getString("duedate"));
				memConsole.setText((String)allMembers.getString("assimem"));
				teamConsole.setText((String)allMembers.getString("assiteam"));
				statusConsole.setText((String)allMembers.getString("status"));
				System.out.println("abcde" + name + des);
				
			}	
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
		
	/* 
	 * Functions present in teams tab
	 * 
	 */

	@FXML	
	private void addToTeam(ActionEvent event) {
		
	}
	
	@FXML	
	private void removeFromTeam(ActionEvent event) {
		
	}
	
	@FXML	
	private void newTeamName() {                  //To check the input in the new team name text box
		if(isOnline)
			addToListTeam.setDisable(false);
		removeFromListTeam.setDisable(true);
		if(newTeamName.getText().equals(""))
			addToListTeam.setDisable(true);
	}
	
	
	@FXML
	private void removeFromListTeam (ActionEvent event) { 	// To remove the selected team 
		String temp = newTeamName.getText();
		
		try {
	        String sql = "delete from teamnames where teamname ='" + temp + "'";
	        conn.myStmt.executeUpdate(sql);
	        updateTeams();
	        newTeamName.setText("");
	        
	        
		}catch(Exception e) {
            System.out.println(e);
        }
	}
	
	@FXML
	private void addToListTeam(ActionEvent event) {
		String temp = newTeamName.getText();
		
		try {
	        String sql = "insert into teamnames (teamname) values('" + temp + "')";
	        System.out.println(sql);
	        conn.myStmt.executeUpdate(sql);
	        updateTeams();
	        newTeamName.setText("");
	        
	        
		}catch(Exception e) {
            System.out.println(e);
        }
        
	}
	
	@FXML	
	private void getTeam() {
		newTeamName.setText((String) teamsList.getSelectionModel().getSelectedItem());
		if(isOnline)
			removeFromListTeam.setDisable(false);
		addToListTeam.setDisable(true);
		updateTeamEmps((String) teamsList.getSelectionModel().getSelectedItem());
		
	}
	
	@FXML
	private void getCurrentMember() {
		if(isOnline)
			removeMemberFromTeam.setDisable(false);
	}
	
	
	/* 
	 * functions present in members tab
	 * 
	 */
	
	@FXML	
	private void getName() {
		String selectedName = (String) membersList.getSelectionModel().getSelectedItem();
		nameInput.setText(selectedName);
		if(isOnline)
			removeFromList.setDisable(false);
		addToList.setDisable(true);
		updateEmpTeams(selectedName);
		addToTeam.setDisable(true);
		removeFromTeam.setDisable(true);
		
	}
	
	@FXML	
	private void getAvailableTeam() { 
		if(isOnline)
		addToTeam.setDisable(false);
		
	}
	
	@FXML	
	private void getCurrentTeam() { 
		if(isOnline)
		removeFromTeam.setDisable(false);
		
	}

	@FXML	
	private void nameTyped() {					//To check the input in the new member name text box
		if(isOnline)
			addToList.setDisable(false);
		removeFromList.setDisable(true);
		if(nameInput.getText().equals(""))
			addToList.setDisable(true);
	}
	
	
	@FXML
	private void removeFromList (ActionEvent event) {
		String temp = nameInput.getText();
		
		try {
	        String sql = "delete from empnames where empname ='" + temp + "'";
	        conn.myStmt.executeUpdate(sql);
	        nameInput.setText("");
	        updateMembers();
	        updateEmpTeams(temp);
	        
	        
		}catch(Exception e) {
            System.out.println(e);
        }
		
	}
	
	@FXML
	private void addToList(ActionEvent event) {
		String temp = nameInput.getText();
		
		try {
			System.out.println(temp);
			//conn.myStmt = conn.myConn.createStatement();
			System.out.println(temp + " 2");
	        String sql = "insert into empnames (empname) values('" + temp + "')";
	        System.out.println(sql);
	        conn.myStmt.executeUpdate(sql);
	        System.out.println("Item created3");
	        System.out.println("Item created");
	        updateMembers();
	        nameInput.setText("");
	        addToList.setDisable(true);
	        
	        
		}catch(Exception e) {
            System.out.println(e);
        }
        
	}
	
	@FXML
	private void addMemberToTeam(ActionEvent event) {
		String selectedName = (String) membersList.getSelectionModel().getSelectedItem();
		String selectedTeam = (String) availableTeams.getSelectionModel().getSelectedItem();
		try {
	        String sql = "insert into empteam (empf, teamf) values('" + selectedName + "','" + selectedTeam + "')";
	        System.out.println(sql);
	        conn.myStmt.executeUpdate(sql);
	        updateMembers();
	        
	        
		}catch(Exception e) {
            System.out.println(e);
        }
		
		updateEmpTeams(selectedName);
		addToTeam.setDisable(true);
	}
	
	@FXML
	private void removeMemberFromTeam(ActionEvent event) {
		String selectedName = (String) membersList.getSelectionModel().getSelectedItem();
		String selectedTeam = (String) currentTeams.getSelectionModel().getSelectedItem();
		try {
	        String sql = "delete from empteam where empf ='" + selectedName + "' and teamf = '" + selectedTeam + "'";
	        System.out.println(sql);
	        conn.myStmt.executeUpdate(sql);
	        updateMembers();
	        
	        
		}catch(Exception e) {
            System.out.println(e);
        }
		
		updateEmpTeams(selectedName);
		removeFromTeam.setDisable(true);
	}
		/*
		 * Intialization 
		 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		updateMembers();
		removeFromList.setDisable(true);
		addToList.setDisable(true);
		removeFromListTeam.setDisable(true);
		addToListTeam.setDisable(true);
		addToTeam.setDisable(true);
		removeFromTeam.setDisable(true);
		addMemberToTeam.setDisable(true);
		removeMemberFromTeam.setDisable(true);
		createNewAction.setDisable(true);
		clearAction.setDisable(true);
		updateAction.setDisable(true);
		deleteAction.setDisable(true);
		updateAction.setDisable(true);
		updateMembers();
		updateTeams();
		updateActions();
		updateConsole();
		
		new Timer().schedule(
		   new TimerTask() {
			   @Override
		        public void run() {
		            String Status = new InternetConnectivity_java().getInternetStatus();
	            	System.out.println("Status:" + Status );
	            	if(Status.equals("Online")) {
	            		circleDesign.setFill(Color.GREEN);
	            		circleDesign1.setFill(Color.GREEN);
	            		circleDesign2.setFill(Color.GREEN);
	            		circleDesign3.setFill(Color.GREEN);
	            		isOnline = true;
	            		System.out.println("online");
	            	}
	            	else {
	            		circleDesign.setFill(Color.RED);
	            		circleDesign1.setFill(Color.RED);
	            		circleDesign2.setFill(Color.RED);
	            		circleDesign3.setFill(Color.RED);
	            		isOnline = true;
	            		System.out.println("online");
	            		removeFromList.setDisable(true);
	            		addToList.setDisable(true);
	            		removeFromListTeam.setDisable(true);
	            		addToListTeam.setDisable(true);
	            	}
		        }
		    }, 0, 5000);
		}

	/*
	 * functions to refresh data
	 * 
	 */
	public void updateMembers() {
		try {
			ResultSet allMembers = conn.myStmt.executeQuery("select * from empnames");
			ObservableList<Object> membersLists = FXCollections.observableArrayList();
			while (allMembers.next()) {
				membersLists.add(allMembers.getString("empname"));
				//memberList = new ListView<String>();
				membersList.setItems(membersLists);
			}
			
		}
		catch (Exception e) {
			
		}
	}
	
	
	public void updateTeams() {
		try {
			ResultSet allMembers = conn.myStmt.executeQuery("select teamname from teamnames");
			ObservableList<Object> membersLists = FXCollections.observableArrayList();
			while (allMembers.next()) {
				membersLists.add(allMembers.getString("teamname"));
				
			}
			teamsList.setItems(membersLists);
			
		}
		catch (Exception e) {
			
		}
	}
	
	public void updateEmpTeams(String employeeName) {
		try {
			String out = "select teamname from teamnames where teamname not in( select teamf from empteam where empf = '"+ employeeName + "')";
			System.out.println(out);
			ResultSet allMembers = conn.myStmt.executeQuery (out);
			ObservableList<Object> membersLists = FXCollections.observableArrayList();
			while (allMembers.next()) {
				membersLists.add(allMembers.getString("teamname"));
				System.out.println(allMembers.getString("teamname"));
				
			}
			availableTeams.setItems(membersLists);
			
		}
		catch (Exception e) {
			System.out.println(e + "sdsdsd");
		}
		
		try {
			String out = "select teamf from empteam where empf  = '"+ employeeName + "'";
			System.out.println(out);
			ResultSet allMembers = conn.myStmt.executeQuery(out);
			ObservableList<Object> membersLists = FXCollections.observableArrayList();
			while (allMembers.next()) {
				membersLists.add(allMembers.getString("teamf"));
				System.out.println(allMembers.getString("teamf"));
			}
			currentTeams.setItems(membersLists);
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	
	public void updateTeamEmps(String selectedTeam) {
		try {
			String out = "select empname from empnames where empname not in( select empf from empteam where teamf = '"+ selectedTeam + "')";
			System.out.println(out);
			ResultSet allMembers = conn.myStmt.executeQuery (out);
			ObservableList<Object> membersLists = FXCollections.observableArrayList();
			while (allMembers.next()) {
				membersLists.add(allMembers.getString("empname"));
				System.out.println(allMembers.getString("empname"));
				
			}
			availableMemberForTeam.setItems(membersLists);
			
		}
		catch (Exception e) {
			System.out.println(e + "sdsdsd");
		}
		
		try {
			String out = "select empf from empteam where teamf  = '"+ selectedTeam + "'";
			System.out.println(out);
			ResultSet allMembers = conn.myStmt.executeQuery(out);
			ObservableList<Object> membersLists = FXCollections.observableArrayList();
			while (allMembers.next()) {
				membersLists.add(allMembers.getString("empf"));
				System.out.println(allMembers.getString("empf"));
			}
			currentMembersInTeam.setItems(membersLists);
			
		}
		catch (Exception e) {
			System.out.println(e);
		}	
		
	}
	
	public void updateActions() {
		try {
			ResultSet allMembers = conn.myStmt.executeQuery("select * from actions");
			ObservableList<Object> membersLists = FXCollections.observableArrayList();
			while (allMembers.next()) {
				membersLists.add(allMembers.getString("actionname"));
				//memberList = new ListView<String>();
			}
			actionsCombo.setItems(membersLists);
			
		}
		catch (Exception e) {
			
		}
	}
	
	
	public void updateConsole() {
		try {
			ResultSet allMembers = conn.myStmt.executeQuery("select * from actions");
			ObservableList<Object> membersLists = FXCollections.observableArrayList();
			while (allMembers.next()) {
				membersLists.add(allMembers.getString("actionname"));
				//memberList = new ListView<String>();
			}
			actionsList.setItems(membersLists);
			
		}
		catch (Exception e) {
			
		}
	}
}