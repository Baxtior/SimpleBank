package banking.datasource;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class DataSource {

    private static final SQLiteDataSource dataSource = new SQLiteDataSource();
    private final String fileName;

    public DataSource(String fileName) {
        this.fileName = fileName;
    }

    public Connection connect() {
        String url = "jdbc:sqlite:C:\\Users\\acer\\IdeaProjects\\Simple Banking System\\Simple Banking System\\task\\" + fileName;
        dataSource.setUrl(url);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card\n" +
                "(\n" +
                "    id      INTEGER PRIMARY KEY,\n" +
                "    number  TEXT NOT NULL,\n" +
                "    pin     TEXT NOT NULL,\n" +
                "    balance INTEGER DEFAULT 0\n" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String number, String pin, int balance) {
        String sql = "INSERT INTO card(number, pin, balance) VALUES(?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            pstmt.setInt(3, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean getCard(String number, String pin) {
        String sql = "SELECT a.number, a.pin FROM card as a WHERE a.number = ? AND a.pin = ?";
        String n = null;
        String p = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    n = resultSet.getString("number");
                    p = resultSet.getString("pin");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return number.equals(n) && pin.equals(p);
    }

    public int getCardId(String number, String pin) {
        String sql = "SELECT id FROM card WHERE number = ? AND pin = ?";
        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public void addIncome(Integer toId, int amount) {
        String sql = "UPDATE card SET balance = balance + ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            pstmt.setInt(1, amount);
            pstmt.setInt(2, toId);
            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transfer(Integer fromId, Integer toId, int amount) {
        String sql1 = "UPDATE card SET balance = balance - ? WHERE id = ?";
        String sql2 = "UPDATE card SET balance = balance + ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

            conn.setAutoCommit(false);

            pstmt1.setInt(1, amount);
            pstmt1.setInt(2, fromId);
            pstmt1.executeUpdate();

            pstmt2.setInt(1, amount);
            pstmt2.setInt(2, toId);
            pstmt2.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getAccountNumber(int id) {
        String sql = "SELECT * FROM card WHERE id = ?";
        String number = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    number = resultSet.getString("number");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return number;
    }

    public int getAccountId(String number) {
        String sql = "SELECT id FROM card WHERE number = ?";
        int id = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public String getAccountPin(int id) {
        String sql = "SELECT * FROM card WHERE id = ?";
        String pin = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    pin = resultSet.getString("pin");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return pin;
    }

    public int getAccountBalance(int id) {
        String sql = "SELECT * FROM card WHERE id = ?";
        int balance = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    balance = resultSet.getInt("balance");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    public boolean containsCard(String number) {
        String sql = "SELECT number FROM card WHERE number = ?";
        String n = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    n = resultSet.getString("number");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return number.equals(n);
    }

    public boolean hasEnoughMoney(Integer id, int amount) {
        String sql = "SELECT balance FROM card WHERE id = ?";
        int balance = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    balance = resultSet.getInt("balance");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return (balance - amount) >= 0;
    }

    public void closeAccount(Integer id) {
        String sql = "DELETE FROM card WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
