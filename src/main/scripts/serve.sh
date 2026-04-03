#! /bin/bash

scriptDir=$(dirname $0)
java -cp "${scriptDir}/lib/*" org.lbpl.lsp.LanguageServerMain
