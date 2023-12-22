
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.Random;
import javax.swing.*;

public class TikTokViewsCounter {

    private static WebDriver driver;
    private static Random random = new Random();

    public static void main(String[] args) {
        // Set the path to your ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\97254\\Desktop\\chromedriver.exe");

        // Create the user interface
        JFrame frame = new JFrame("TikTok Views Counter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel urlLabel = new JLabel("Enter TikTok profile URL:");
        JTextField urlInput = new JTextField(30);
        JButton startButton = new JButton("Start");
        JTextArea logOutput = new JTextArea(10, 30);

        startButton.addActionListener(e -> {
            String url = urlInput.getText();
            if (url.isEmpty()) {
                showMessage("Please enter a valid TikTok profile URL.");
                return;
            }

            logOutput.setText("Initializing...\n");
            driver = new ChromeDriver();

            try {
                driver.get(url);
                Thread.sleep(5000); // Wait for page load

                // Enter a random video in the profile
                WebElement videoElement = driver.findElement(By.cssSelector(".video-feed-item"));
                videoElement.click();
                Thread.sleep(5000); // Wait for video to load

                int initialViews = getViews();
                int currentViews = initialViews;
                logOutput.append("Initial Views: " + initialViews + "\n");

                for (int i = 0; i < 1000; i++) {
                    scrollPage();
                    currentViews = getViews();
                    logOutput.append("Scroll " + (i+1) + ", Views: " + currentViews + "\n");
                }

                logOutput.append("Final Views: " + currentViews + "\n");

            } catch (Exception ex) {
                showMessage("An error occurred: " + ex.getMessage());
            } finally {
                if (driver != null) {
                    driver.quit();
                }
            }
        });

        frame.add(urlLabel);
        frame.add(urlInput);
        frame.add(startButton);
        frame.add(logOutput);

        frame.setVisible(true);
    }

    private static void scrollPage() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try {
            // Pause for a random duration between 1 and 3 seconds
            Thread.sleep((random.nextInt(3 - 1 + 1) + 1) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int getViews() {
        WebElement viewsElement = driver.findElement(By.cssSelector(".video-meta-count > span"));
        String viewsString = viewsElement.getText().replaceAll("[^0-9]", ""); // Remove non-digit characters
        return Integer.parseInt(viewsString);
    }

    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}


