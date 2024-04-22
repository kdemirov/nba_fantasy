# Nba Fantasy

Web application that allows users to choose their own team from different nba teams
create groups, invite users into groups and have fun, for all nba fans.

## Nba Fantasy Data

There is admin part for fetching the needed data check java docs for detailed
information, or you can import the postgres.dmp file in your database by running
the scripts for windows and linux:

## Windows

Make sure that you docker container is running with the command:
`docker container ls`
Find the docker container id, and run the script with modified variables:
`./windows_import_dump_file_script.ps1 container_id`

## Linux

Again find the id and run the following command:
`./linux_import_dump_script.sh container_id`

## Nba Fantasy Admin

Define your admin user directly into database with your own password, in order to fetch the
games that are played and provide information about game details url, box score link from nba.com/schedule,
the game details url must start with '/' exclude nba.com from the url and the system will calculate
fantasy points for players per game.

In order to start the project you need to define system environment variables
`SPRING_DATASOURCE_USERNAME = database user username`
`SPRING_DATASOURCE_PASSWORD = database user password`
`NBA_FANTASY_MAIL = your mail from which you want to send confirmation code for verifying the registered user account`
`NBA_FANTASY_PASSWORD = your mail's password`
`WEB_DRIVER_PATH = copy absolute path from resources/geckodriver-v0.33.0-win64/geckodriver.exe`

`Note: This project is done for educational purposes and for all nba fans to have fun.`
