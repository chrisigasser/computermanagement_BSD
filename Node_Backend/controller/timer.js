var mongoUri = 'mongodb://localhost:27017/BSD';
var MongoClient = require('mongodb').MongoClient;
var ObjectID = require('mongodb').ObjectID;

exports.addTimerEntry = function (req, res) {
    try {
        console.log(req.body);
        if (req.body != undefined&&req.body.pcname!=undefined&&req.body.starttime!=undefined&&req.body.endtime!=undefined) {
            var pcname = req.body.pcname;
            var starttime = req.body.starttime;
            var endtime =  req.body.endtime;
            myobj = {
                pcname: pcname,
                starttime: starttime,
                endtime: endtime
            };

            MongoClient.connect(mongoUri, function (err, db) {
                if (err) throw err; 
                db.collection("timer").insertOne(myobj, function (err, insertresult) {
                    if (err) throw err;
                    res.send(insertresult.insertedId);
                });
            });
        }
    } catch (error) {
        console.log('oh no exception');
    }
}
