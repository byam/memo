## Installing Java

① Download from here
- http://www.oracle.com/technetwork/java/javase/downloads/index.html

② Install by clicking downloaded `.dmg` file.
```
# check java
java -version
```

③ Set `JAVA_HOME` to environment variable
```sh
export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/"
```

## Installing Maven

① Download from here
- http://maven.apache.org/download.cgi

② Unzip and add `bin` to `PATH`
```sh
# unzip
tar xzvf apache-maven-3.5.0-bin.tar.gz

# add Maven bin to PATH
export PATH="/Users/01014477/apache-maven-3.5.0/bin/:$PATH"

# check Maven
mvn --version
```
