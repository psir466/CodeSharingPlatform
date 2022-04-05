<!DOCTYPE html>
<html lang="en">
<head>
     <meta charset="utf-8">
    <title>Latest</title>
     <link rel="stylesheet" href="/style.css">
     <link rel="stylesheet"
            href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
     <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
     <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
<ul>
    <#list codeList as code>
        <li>
            <span id="load_date">${code.date}</span>
            <pre id="code_snippet"><code>${code.code}</code></pre>
        </li>
    </#list>
</ul>
</body>
</html>