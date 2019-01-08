# narration-box!

A REST API built over a framework to automatically generate states of characters in a story. 

Steps to build and Install (Without docker):
1. cmd: maven clean install
2. Generate an app on heroku and point the app as an remote repo for this project.
3. Set required Environment.variables/Config.Vars on Heroku app either using Dashboard or Command Line
4. cmd: git push heroku {current_branch_name}:master

Environment variables to set: MONGO_DB_USERNAME, MONGO_DB_PASSWORD

URL of current stable version: https://narration-box.herokuapp.com

Swagger documentation:  
Swagger UI: https://narration-box.herokuapp.com/swagger-ui.html  
API Doc: https://narration-box.herokuapp.com/v2/api-docs  

