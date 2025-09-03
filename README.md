# book_recommendation
just a part of recommendation algorithm
You can view my complete book e-commerce and recommendation algorithm in my book_point_final repository

Run this applicaton
in the browser enter this
localhost:8080/api/recommendations/load?filePath=src/main/resources/book_dummy_ver2.csv
you should be able to get a message that the file has been loaded

Enter the book id of the book you want to look up
http://localhost:8080/api/recommendations/{enter book id}
you wil get the detailed info of the entered book
with 5 books most similar to that input book
