var express = require('express');
var app = express();
var path = require('path');
var bodyParser = require('body-parser')

const indexRouter = require('./routes/index');
app.use('/', indexRouter);
const routes = require('./routes');

app.use('/', routes);
var jsonParser = bodyParser.json();

app.use(bodyParser.urlencoded())
app.use(jsonParser);

app.use(express.static('static'));
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);


var server = app.listen(3000, function(){
    console.log("Express server has started on port 3000");
});