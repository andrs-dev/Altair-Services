# Installation
## Installing newman
> npm install -g newman

## Installing report-type
> npm install -g newman-reporter-htmlextra

## Installing NodeJs
Minimum version is 1.18

### Via NVM
> nvm install 18

### Via NodeSource
> curl -sL https://deb.nodesource.com/setup_18.x | sudo -E bash -
> sudo apt install nodejs

## Configuration
You'll need to go to src/main/resources/application.properties. 
There you need to write the absolute path of the 
dependencies. Also,
you need the path of these directories:
- Api Directory (src/main/resources/static/api),
- HTML reports directory 
(src/main/resources/static/api/reports/htmlextra),
- XML reports directory (src/main/resources/static/api/reports/xml) 

and put it in 
src/main/resources/application.properties
