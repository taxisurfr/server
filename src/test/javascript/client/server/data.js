module.exports = {
    colomboAirportArugamBayRoute: getColomboAirportArugamBayRoute(),
    colomboAirportWeligamaRoute: getColomboAirportWeligamaRoute(),
    payment: payment(),
    booking: addBooking()
};

function getColomboAirportArugamBayRoute() {
    return {
        id: 1,
        startroute: 'Colombo Airport',
        endroute: 'Arugam Bay',
        cents: 16000,
        description: 'nice ride to arugam bay',
        pickupType: 'AIRPORT'
    };
}
function getColomboAirportWeligamaRoute() {
    return {
        id: 2,
        startroute: 'Colombo Airport',
        endroute: 'Weligama',
        cents: 8500,
        description: 'nice ride to weligama',
        pickupType: 'AIRPORT'
    };
}

function addBooking() {
    return {id: 1, name: 'booker name', email: 'booker@email.com'};
}

function payment() {
    return {ok: true, error: ''};
}

