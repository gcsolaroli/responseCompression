# Sample project to highlight an issue serving static content with `zio-http`

Sample project using `scala 3` (3.4.2), `zio 2` (2.1.1), `zio-http` (3.0.0-RC7) to highlight a very weird problem when enabling response compression and serving static content using the `Handlers.fromFile` function.

The sample application is serving the static file located into `src/main/resources/static` folder on two separate paths:
- http://localhost:8080/resources/static/index.html
- http://localhost:8080/directory/static/index.html

When hitting the first URL, the application uses `Middleware.serveResources` to process the request, while the second URL lets the application use `Middleware.serveDirectory` method.

Dumping both responses with `curl`, they look identical:
```
% curl -v http://localhost:8080/resources/static/index.html
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /resources/static/index.html HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.6.0
> Accept: */*
> 
< HTTP/1.1 200 OK
< content-type: text/html
< date: Sun, 19 May 2024 17:38:25 GMT
< content-length: 130
< 
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Page Title</title>
</head>
<body> 
    <h1>Hello!</h1>
</body>
* Connection #0 to host localhost left intact
</html>%
```

```
% curl -v http://localhost:8080/directory/static/index.html
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /directory/static/index.html HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.6.0
> Accept: */*
> 
< HTTP/1.1 200 OK
< content-type: text/html
< date: Sun, 19 May 2024 17:37:13 GMT
< content-length: 130
< 
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Page Title</title>
</head>
<body> 
    <h1>Hello!</h1>
</body>
* Connection #0 to host localhost left intact
</html>%
```

But using a regular browser only the first URL works correctly, while trying to access the second URL  the brwoser reports the following error:

> Content Encoding Error
> The page you are trying to view cannot be shown because it uses an invalid or unsupported form of compression.

## Disabling compression

If response compression is disabled (it's enough to comment out Mail.scala#L14), both URLs work correctly when accessed with a browser.

