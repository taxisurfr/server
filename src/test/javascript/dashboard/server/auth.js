var router = require('express').Router();
var four0four = require('./utils/404')();
var data = require('./data');
var jwt = require('jwt-simple');

router.post('/google', getGoogleAuth);

module.exports = router;

//////////////

function getGoogleAuth(req, res, next) {
    // {code: "4/wdPpdbUbK9hb4Q2jfpN3vtM0ncXdxBoZACIHFtQvfvc",…}
    // clientId
    //     :
    //     "468150866643-hvi544b3jp0bjptd444gsg365nd576j6.apps.googleusercontent.com"
    // code
    //     :
    //     "4/wdPpdbUbK9hb4Q2jfpN3vtM0ncXdxBoZACIHFtQvfvc"
    // redirectUri
    //     :
    //     "http://localhost:3000"
    // state
    //     :
    //     "7qh3hfmpxtysq48dks82i0ms4i"
    var token = req.header('Authorization').split(' ')[1];
    console.log('token'+token);
    var payload = jwt.decode(token, '6Nn05IlQQK9-Kk2PTyNZHViR');
    console.log('payload'+payload);

    // var token = 'ya29.Ci9hA0NROtpX_8z6kdfsFfPMKc55FmwuvioDakmJW3IMd68Um3tr5xewx7Y11rK3Mg';
    // var payload = jwt.decode(token, '6Nn05IlQQK9-Kk2PTyNZHViR');
    // console.log('payload'+payload)
    console.log('code'+req.body.code);
    console.log('clientId'+req.body.clientId);
    console.log('state'+req.body.state);
    res.status(200).send(data.authResponse);
}
