module.exports = {
    routes: getRoutes(),
    authResponse: getAuthResponse()
};

function getRoutes() {
    return [
    {id: 1, startroute: 'Colombo Airportx', endroute: 'Arugam Bay', cents: 16000 ,description: 'nice ride to arugam bay', pickupType:'AIRPORT'}
    ];
}

function getAuthResponse() {
    return {routes: 'true'}
    ;
}
