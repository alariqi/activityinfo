
# Setup

## Mac OS X

* Download and install the latest version of the JDK 1.8 from Oracle
* Install MySQL 5.7 or later. See [instructions 
 
## Checking out 

    git clone git@github.com:bedatadriven/activityinfo.git
    git fetch origin
    git branch chdc origin/chdc
    

## Setting passwords

Create a ~/.gradle/gradle.properties file for storing passwords. 

    localMySqlUsername=root
    localMySqlPassword=root

Then run
    
    ./gradlew chdc:database:update 


# Developing

To start the development server, run:

    ./gradlew chdc:server:devMode
    
    
# Deployment 

To deploy to Azure:

    ./gradlew chdc:server:deploy
    

  
