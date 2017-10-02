import React from 'react'
import { Link } from 'react-router-dom'

var style = {
    color: 'white',
    fontSize: 24
};
export default React.createClass({
    render() {
        return <Link style={style} {...this.props}/>
    }
})