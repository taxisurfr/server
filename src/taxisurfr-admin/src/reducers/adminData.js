const adminData = (state = {}, action) => {
    switch (action.type) {
        case 'NEW_FILMS':
            return Object.assign({}, state, {
                films: action.films
            })
        default:
            return state
    }
}

export default adminData
