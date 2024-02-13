import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	public static void Table1() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("************************ Welcome to the Çalık Bank *********************");
		System.out.println("Select an option below\n");
	}

	public static void Table2() {

		System.out.println("1-) Check Balance");
		System.out.println("2-) Withdraw Money");
		System.out.println("3-) Deposit Money");
		System.out.println("4-) Transfer to Another Account");
		System.out.println("5-) Delete Account");
		System.out.println("6-) Change Password");
		System.out.println("7-) Exit / Quit\n");
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		try {
			Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root",
					"Password");
			Statement myStatement = myConnection.createStatement();

			Table1();
			int count3 = 1;
			int temp = 0;
			String name = "";
			int id = 0;
			while (count3 == 1) {
				System.out.println("1-) Login");
				System.out.println("2-) Register\n");
				System.out.print("Enter your choice: ");
				int choice = 0;

				choice = scanner.nextInt();

				if (choice == 1) {
					int IDcheck = 1;
					while (IDcheck == 1) {
						try {
							System.out.print("Enter Your id: ");
							id = scanner.nextInt();
							IDcheck = 0;
						} catch (InputMismatchException e) {
							System.out.print("You Entered an Invalid Id Number. Please Try Again and ");
							scanner.nextLine();
							IDcheck = 1;
						}
					}
					int PasswordCheck = 1;
					int password = 0;
					while (PasswordCheck == 1) {
						try {
							System.out.print("Enter Your Password: ");
							password = scanner.nextInt();
							PasswordCheck = 0;
						} catch (InputMismatchException e) {
							System.out.print("You Entered an Invalid Password. Please Try Again and ");
							scanner.nextLine();
							PasswordCheck = 1;
						}
					}

					ResultSet rSet = myStatement.executeQuery(
							"SELECT * FROM user_info WHERE user_id = " + id + " AND user_password = " + password + "");
					count3 = 0;
					while (rSet.next()) {
						name = rSet.getString(3);
						System.out.println("Welcome back to Çalık Bank " + name + ".");
						System.out.println();
						temp = 1;
						count3 = 0;
					}
				}

				else if (choice == 2) {
					temp = 2;
					String name1 = null;
					String surname = null;
					int password = 0;
					User user = new User(name1, surname, password);
					System.out.print("Enter Your Name: ");
					name1 = scanner.next();
					user.setUserName(name1);
					System.out.print("Enter Your Surname: ");
					surname = scanner.next();
					user.setSurname(surname);

					int ısPassword = 1;
					while (ısPassword == 1) {
						try {
							System.out.print("Enter Your Password(numbers only): ");
							password = scanner.nextInt();
							ısPassword = 0;
						} catch (InputMismatchException e) {
							System.out.print("you Entered an ınvalid Value. Please Try Again and ");
							scanner.nextLine();
							ısPassword = 1;
						}
					}
					user.setUserPassword(password);
					DatabaseOperations dbRegister = new DatabaseOperations();
					dbRegister.registerUser(user.getUserID(), name1, surname, password, user.getBalance());
					user.showUserInfo();
					count3 = 1;

				}
			}
			int count = 1;
			while (count == 1) {
				if (temp == 1) {
					Table2();
					System.out.print("Enter your choice: ");
					String choice2 = scanner.next();
					double balance = 0.0;
					ResultSet balanceResultSet = myStatement
							.executeQuery("SELECT user_balance FROM user_info WHERE user_id = " + id);
					if (balanceResultSet.next()) {
						balance = balanceResultSet.getDouble("user_balance");

						switch (choice2) {
						case "1":
							System.out.println("Your Current Balance is: " + balance + "TL");
							System.out.println();
							break;
						case "2":
							System.out.print("Please Enter the Amount You Want to Withdraw.");
							double withdraw = scanner.nextDouble();

							if (withdraw > balance) {
								System.out.println("Insufficient funds. Withdrawal canceled.");
								System.out.println();
							} else {
								System.out.println("Withdrawing " + withdraw + " TL...");
								balance -= withdraw;
								System.out.println("Successfully Withdrawn!");
								System.out.println("Your Updated Balance is: " + balance);
								System.out.println();

								myStatement.executeUpdate(
										"UPDATE user_info SET user_balance = " + balance + " WHERE user_id = " + id);
							}
							break;
						case "3":
							System.out.println("Please Enter the Amount You Want to Deposit.");
							double deposit = scanner.nextDouble();

							System.out.println("Depositing " + deposit + "TL...");
							balance += deposit;
							System.out.println("Successfully Deposit!");
							System.out.println("Your Updated Balance is: " + balance);
							myStatement.executeUpdate(
									"UPDATE user_info SET user_balance = " + balance + " WHERE user_id = " + id);
							break;

						case "4":
							System.out.println("Please Enter the Account ID Number You Want to Transfer to: ");
							int transferID = scanner.nextInt();

							System.out.println("Please Enter the Amount You Want to Transfer: ");
							double transferAmount = scanner.nextDouble();

							ResultSet targetAccountResultSet = myStatement
									.executeQuery("SELECT * FROM user_info WHERE user_id = " + transferID);
							if (targetAccountResultSet.next()) {
								double targetBalance = targetAccountResultSet.getDouble("user_balance");

								if (transferAmount > balance) {
									System.out.println("Insufficient funds. Transfer canceled.");
								} else {
									balance -= transferAmount;

									myStatement.executeUpdate("UPDATE user_info SET user_balance = " + balance
											+ " WHERE user_id = " + id);

									targetBalance += transferAmount;

									myStatement.executeUpdate("UPDATE user_info SET user_balance = " + targetBalance
											+ " WHERE user_id = " + transferID);

									System.out.println("Successfully Transferred " + transferAmount
											+ " TL to account ID " + transferID);
									System.out.println("Your Updated Balance is: " + balance);
								}
							} else {
								System.out.println("Target account not found. Transfer canceled.");
							}
							break;

						case "7":
							System.out.println("Thank You for Using Çalık Bank. See You Again..");
							count = 0;
							break;
						case "5":
							System.out.print(
									"Are you sure you want to delete your account? This action is irreversible. (yes/no): ");
							String confirmation = scanner.next().toLowerCase();

							if (confirmation.equals("yes")) {

								int enteredPassword = 0;
								int passwordCheck = 1;
								while (passwordCheck == 1) {
									try {
										System.out.print("Enter your password for Confirmation: ");
										enteredPassword = scanner.nextInt();
										passwordCheck = 0;
									} catch (InputMismatchException e) {
										System.out.print("Invalid password format. Please enter a valid password and ");
										scanner.nextLine();
										passwordCheck = 1;
									}
								}

								ResultSet passwordResultSet = myStatement
										.executeQuery("SELECT * FROM user_info WHERE user_id = " + id
												+ " AND user_password = " + enteredPassword);

								if (passwordResultSet.next()) {
									myStatement.executeUpdate("DELETE FROM user_info WHERE user_id = " + id);
									System.out.println(
											"Your Account has been Deleted. Thank You for Being a Çalık Bank Customer.");
									count = 0;
								} else {
									System.out.println("Incorrect Password. Account Deletion Canceled.");
									System.out.println();
								}
							} else {
								System.out.println("Account Deletion Canceled.");
							}
							break;
						case "6":
							int oldPassword = 0;
							int passwordCheck2 = 1;
							while (passwordCheck2 == 1) {
								try {
									System.out.print("Enter your password for Confirmation: ");
									oldPassword = scanner.nextInt();
									passwordCheck2 = 0;
								} catch (InputMismatchException e) {
									System.out.print("Invalid Password Format. Please Enter a Valid Password and ");
									scanner.nextLine();
									passwordCheck2 = 1;
								}
							}
							ResultSet oldPasswordResultSet = myStatement
									.executeQuery("SELECT * FROM user_info WHERE user_id = " + id
											+ " AND user_password = " + oldPassword);
							if (oldPasswordResultSet.next()) {
								int passwordCheck3 = 1;
								int newPassword = 0;
								while (passwordCheck3 == 1) {
									try {
										System.out.print("Enter Your New Password: ");
										newPassword = scanner.nextInt();
										passwordCheck3 = 0;
									} catch (InputMismatchException e) {
										System.out.print("Invalid password format. Please enter a valid password and ");
										scanner.nextLine();
										passwordCheck3 = 1;
									}
								}

								int passwordCheck4 = 1;
								int newPassword2 = 0;
								while (passwordCheck4 == 1) {
									try {
										System.out.print("Enter Your New Password Again: ");
										newPassword2 = scanner.nextInt();
										passwordCheck4 = 0;
									} catch (InputMismatchException e) {
										System.out.print("Invalid password format. Please ");
										scanner.nextLine();
										passwordCheck4 = 1;
									}
								}

								if (newPassword == newPassword2) {
									myStatement.executeUpdate("UPDATE user_info SET user_password = " + newPassword
											+ " WHERE user_id = " + id);
									System.out.println("Your Password has been Successfully Changed!");
								} else {
									System.out.println("Passwords do not Match. Password Change Canceled.");
								}
							} else {
								System.out.println("Incorrect Password. Password Change Canceled.");
							}
							break;

						default:
							System.out.println("You Entered an Incorrect Value. Please Try Again");
							continue;
						}

					} else {
						System.out.println("Balance Not Found for the Logged-in User.");
					}
				} else if (temp == 0) {
					System.out.println("Invalid ID or Password. Please Try Again...");
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Database Connection Error: " + e.getMessage());
		}
		scanner.close();
	}
}
