const auth = (state = {}, action) => {
    switch (action.type) {
        case 'NEW_LOGIN':
            return Object.assign({}, state, {
                text: action.username
            })
        default:
            return state
    }
}

export default auth
