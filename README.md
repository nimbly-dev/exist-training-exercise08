
# exist-training-exercise08

## To run the project: 

    1. First open pgAdmin or any query tool and run this query statements

        
          INSERT INTO roles(name) VALUES('ROLE_USER');
          INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
          INSERT INTO roles(name) VALUES('ROLE_ADMIN'); 
    
    2. Open exercise08 which is the backend directory, then using
       your command-line enter command: "mvn spring-boot:run"

    3. Open exercise08-frontend which is the frontend directory,
       then using your command-line enter command: "ng serve --open"
    
    4. An tab will be opened by your default browser navigating you to
       the starting page.
