var express = require('express');
var app = express();
var bodyParser = require('body-parser')
var mongoUri = 'mongodb://localhost:27017/guidemanagement';
var MongoClient = require('mongodb').MongoClient;
var ObjectID = require('mongodb').ObjectID;

var timer = require('./controller/timer');

var cors = require('cors');
app.use(cors());
app.use(bodyParser.json());

app.post('/addTimerEntry', timer.addTimerEntry);

app.listen(3000);
console.log("Server up and running on port 3000");