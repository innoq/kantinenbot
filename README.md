kantinenbot
===========

Parses some Sources (e.g. Braas Excel Sheet) and creates different Outputs (MD and JSON atm.)


## Build

    mvn clean install

## Usage

    kantinenbot.jar <dd.mm.yyyy> *.xls -o <outputFolder>
    
## Example

    java -jar target/kantinenbot.jar 30.09.2013 /Users/user/Desktop/Speiseplan\ Braas.xls -o /tmp/
    writing to /tmp/
	    wrote /tmp/README.json
	    wrote /tmp/README.md
