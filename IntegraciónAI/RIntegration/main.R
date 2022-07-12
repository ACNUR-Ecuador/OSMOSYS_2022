#install.packages("devtools")
#library(devtools)
# devtools::install_github("bedatadriven/activityinfo-R")

# load package
library(activityinfo)
# provide credential

#activityInfoLogin("salazart@unhcr.org","ad8b23e965405a53eab1c5bae9a31065")

dbs = getDatabases()

typeof(dbs)
print(length(dbs))


dbsch=getDatabaseSchema("c7qgckzkykaan9v5")
print(typeof(dbsch))
dbTree=getDatabaseTree("c7qgckzkykaan9v5")
print(typeof(dbTree))