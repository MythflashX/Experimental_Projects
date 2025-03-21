<!DOCTYPE html>
<html>
  <body>
    <h1>URL SHORTENER</h1>

    <?php   /* CREATE Database and Table */
    $servername = "localhost";
    $username = "username";
    $password = "password";
    $dbname = "urldb";
    
    try {     // we are using PDO because....why not
      error_reporting(E_ALL);
      ini_set('display_errors', 1);

    /* CREATE DATABASE */
      $conn = new PDO("mysql:host=$servername", $username, $password);
      $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
      
      $sql = "CREATE DATABASE IF NOT EXISTS $dbname";
      $conn->exec($sql);
      echo "<br> Database $dbname Created successfully<br>";

    /* CREATE TABLE */
      $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
      $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

      $sql = "CREATE TABLE IF NOT EXISTS url_table(
        id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        original VARCHAR(30) NOT NULL,
        short VARCHAR(30) NOT NULL,
        reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
      )";
    
      echo "<br> Table url_table Created successfully<br><br>";

      $conn->exec($sql);

    } catch (PDOException $e) {
      echo "<br>" . $sql . "<br>" . $e->getMessage();
    }
    ?>
    

    <form action="experiment.php" method="GET">
      Submit URL: <textarea name="url" rows="1" cols="50"></textarea>
      <input type="submit">
    </form>
    
    
<?php
    function generate_hash ($url)   // get the original url, convert them to numbers and compute them to get unique string of numbers...better than random imo
    {
      $result = 0;
      $weight = 1;              // to take order of characters in equation  (hate to admit it..i was comparing approach with chatgpt and this weight technique was wayy too cool to ignore it)
      foreach ($url as $i) {
        $result += (ord($i) * $weight++);
        //echo $i . ' ' . ord($i) . "<br>";
      }

      //echo "<br>" . $result . "<br>";
      return $result;
    }

    function shortened ($hash)  // conver the string of number to characters
    {
      //echo "<br>SHORTENED<br>";
      $temp = 0;
      $short = "paw.";
      while ($hash > 0) {
        //echo $hash . ' ';
        if ($hash > 10) {   // 2 digits for 1 character (orelse it won't be short)
          $temp = (int)($hash % 100);
          $hash = (int)($hash / 100);
        } else {
          $temp = (int)($hash % 10);
          $hash = (int)($hash / 10);
        }

        if ($temp < 65) {   // map the value so it fit between A-Z and a-z
          $temp += 65;  
        }
        if ($temp > 65 && $temp < 97) {
          $temp += 32;
        }

        $short = $short . chr($temp);
    
        //echo $temp . ' ' . $short . '<br>';
      }
      return $short;
    }

    function links_to_the_past ($url, $original)    // make the link clickable and once click redirect to original link
    {
      return "<a href='$original'>$url</a>";
    }


    
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);   // start a new connection to database
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $conn->beginTransaction();
    
    echo "<br><br>";
    if ($_SERVER['REQUEST_METHOD'] === 'GET') {     // get the url by GET method because again...why not?
      $original = filter_input(INPUT_GET, 'url', FILTER_SANITIZE_URL);  // sanitize input
      
      if (filter_var($original, FILTER_VALIDATE_URL) === false) {   // validate input url
        echo "Invalid URL";
      } else {
        echo $original . "<br>";
        $hash = generate_hash(str_split((string)$original));
        $short = shortened($hash);

        // check if the url already exist in database
        $stmt = $conn->prepare("SELECT short FROM url_table WHERE original = ?");
        $stmt->execute([$original]);
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        if (!$result) {   // register if it is new url
            $stmt = $conn->prepare("INSERT INTO url_table (original, short) VALUES (?, ?)");
            $stmt->execute([$original, $short]);
            $conn->commit();
            echo "<br><br> New URL registered in the Database <br><br>"; 
        } else {
            echo "<br><br> URL is already registered in the Database <br><br>";
        }
      }
    }

    // Display Links
    $stmt = $conn->prepare("SELECT id, original, short FROM url_table");
    $stmt->execute();
    $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

    foreach ($result as $row) {
      echo "ID: " . $row['id'] . ": ";
      echo "Original URL: " . $row['original'] . " : ";
      echo "Shortened URL: " . links_to_the_past($row['short'], $row['original']) . "<br>";
    }
    

    echo "<br><br><h2>EOS</h2>";
?>
  </body>
</html>
