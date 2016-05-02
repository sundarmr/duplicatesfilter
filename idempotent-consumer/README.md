This example requires a bit of clean up in terms of properties to be considered 
seriously , but the intention has been to provide a example which explains and help 
developers realize the potential of camel and its components and when used in conjunction
how it can help a tedious tasks to mudance jobs

Create the below tables in your mysql DB

create table orders (
 orderid varchar(100),
 customername varchar(100)
 );
 
 create table product(
  productname varchar(100),
  skuid varchar(100),
  price double,
  orderid varchar(100)
 );
 
 Run the script by changing the file directory value in  file 
 FileCreator.java class and also 
 by copying it to the from in the configure method of the IdempotentFileConsumer.java class
 
 You can create a file with  n no of records by changing the noOfRecords Tag and also creating 
 duplicates every duplicateInterval( provide zero if you do not want duplicates
  value for the records.
 
