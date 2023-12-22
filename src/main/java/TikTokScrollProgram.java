import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TikTokScrollProgram extends JFrame {
    private JTextField profileLinkField;
    private JButton startButton;
    private JLabel viewsLabel;

    public TikTokScrollProgram() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\97254\\Desktop\\chromedriver.exe");
        setTitle("TikTok Scroll Program");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel profileLinkLabel = new JLabel("Profile Link:");
        profileLinkLabel.setBounds(50, 50, 100, 30);
        panel.add(profileLinkLabel);

        profileLinkField = new JTextField();
        profileLinkField.setBounds(150, 50, 200, 30);
        panel.add(profileLinkField);

        viewsLabel = new JLabel("Views: ");
        viewsLabel.setBounds(150, 100, 200, 30);
        viewsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(viewsLabel);

        startButton = new JButton("Start");
        startButton.setBounds(150, 150, 100, 30);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String profileLink = profileLinkField.getText();
                startScrolling(profileLink);
            }
        });
        panel.add(startButton);

        add(panel);
        setVisible(true);
    }

    private void startScrolling(String profileLink) {
        new Thread(() -> {
            WebDriver driver = new ChromeDriver();
            try {
                driver.get(profileLink);

                WebElement video = driver.findElement(By.xpath("//div[@class='video']"));
                video.click();

                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

                int initialViews = getViewsCount(driver);
                int finalViews = 0;
                for (int i = 0; i < 1000; i++) {
                    jsExecutor.executeScript("window.scrollBy(0, -500)");
                    jsExecutor.executeScript("window.scrollBy(0, 500)");

                    // Update views count
                    finalViews = getViewsCount(driver);
                    viewsLabel.setText("Views: " + finalViews);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                viewsLabel.setText("Initial Views: " + initialViews + " | Final Views: " + finalViews);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                driver.quit();
            }
        }).start();
    }

    private int getViewsCount(WebDriver driver) {
        WebElement viewsElement = driver.findElement(By.xpath("//span[@class='video-meta-count']"));
        String viewsText = viewsElement.getText().replaceAll("[^0-9]", "");
        return Integer.parseInt(viewsText);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TikTokScrollProgram());
    }
}