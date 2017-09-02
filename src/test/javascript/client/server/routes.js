var router = require('express').Router();
var four0four = require('./utils/404')();
var data = require('./data');

router.post('/session.get', getSession);
router.post('/route', getRoute);
router.post('/payment', payment);
router.post('/booking', addBooking);
router.get('/routes', getRoutes);
router.get('/person/:id', getPerson);
router.get('/*', four0four.notFoundMiddleware);

module.exports = router;

//////////////

function getSession(req, res, next) {
    var r = {
        stripePublishable: 'pk_test_rcKuNpP9OpTri7twmZ77UOI5',
        route: null
    };

    if (req.body.url.indexOf('http://localhost:3000/arugambay') != -1) {
        r.route = data.colomboAirportArugamBayRoute;
    }

    if (req.body.url.indexOf('http://localhost:3000/weligama') != -1)
        r.route = data.colomboAirportWeligamaRoute;


    res.status(200).send(r);

}

function getRoutes(req, res, next) {
    res.status(200).send(data.people);
}

function getRoute(req, res, next) {
    res.status(200).send(data.colomboAirportArugamBayRoute);
}

function addBooking(req, res, next) {
    // console.log(req.body);
    res.status(200).send(req.body);
}

function payment(req, res, next) {
    res.status(200).send(data.payment);
}

function getPerson(req, res, next) {
    var id = +req.params.id;
    var person = data.people.filter(function (p) {
        return p.id === id;
    })[0];

    if (person) {
        res.status(200).send(person);
    } else {
        four0four.send404(req, res, 'person ' + id + ' not found');
    }
}
