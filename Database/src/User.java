import java.util.Random;

public class User extends IdChecker {
	private int userID;
	private int UserPassword;
	private String userName;
	private String surname;
	private double balance;

	public User(String userName, String surname, int UserPassword) {

		this.userName = userName;
		this.surname = surname;
		this.UserPassword = UserPassword;
		try {
			this.userID = generatedID();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.balance = 0.0;

	}

	private int generatedID() {
		Random random = new Random();
		int randomID = 0;
		try {
			do {
				randomID = random.nextInt(900000) + 100000;
			} while (!(IdChecker.isUnique(randomID)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return randomID;
	}

	public void showUserInfo() {
		System.out.println();
		System.out.println("Name: " + userName);
		System.out.println("Surname: " + surname);
		System.out.println("id: " + userID);
		System.out.println("password: " + UserPassword);
		System.out.println("balance: " + balance);
		System.out.println();
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getUserPassword() {
		return UserPassword;
	}

	public void setUserPassword(int userPassword) {
		UserPassword = userPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
