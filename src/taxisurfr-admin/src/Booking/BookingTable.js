var TableStore = require('./../util/TableStore');
var FixedDataTable = require('fixed-data-table');
import React, {PropTypes} from 'react'
import {Button} from 'react-bootstrap';

// var IntlMixin     = ReactIntl.IntlMixin;
import {FormattedDate} from 'react-intl';

const {Table, Column, ColumnGroup, Cell} = FixedDataTable;

const TextCell = ({rowIndex, data, col, ...props}) => (
    <Cell {...props}>
        {data.getObjectAt(rowIndex)[col]}
    </Cell>
);
const LinkCell = ({rowIndex, data, col, ...props}) => (
    <Cell {...props}>
        <a href={'https://taxisurfr-taxisurfr.rhcloud.com/rest/api/form?id='+data.getObjectAt(rowIndex)[col]}>booking form</a>
    </Cell>
);

const DateCell = ({rowIndex, data, col, ...props}) => (
    <Cell {...props}>
        <FormattedDate
            value={new Date(data.getObjectAt(rowIndex)[col])}
            day="numeric"
            month="long"
            year="numeric"/>
    </Cell>
);

const ButtonCell = ({rowIndex, data, status,col, ...props}) => (
        <Button disabled={data.getObjectAt(rowIndex)[status] !== 'PAID'}
            onClick={() => props.onClick(data.getObjectAt(rowIndex)[col])}
        >Cancel</Button>
);


class BookingTable extends React.Component {
    constructor(props) {
        super(props);
    };

    render() {
        var theme = {
            disabledStyle: {background: 'gray'},
            overStyle: {background: 'red'},
            activeStyle: {background: 'red'},
            pressedStyle: {background: 'magenta', fontWeight: 'bold'},
            overPressedStyle: {background: 'purple', fontWeight: 'bold'}
        }
        var {bookingList} = this.props;
        var {admin} = this.props;
        var {onCancel} = this.props;
        return (
            <div>
                <Table
                    rowHeight={30}
                    groupHeaderHeight={30}
                    headerHeight={30}
                    rowsCount={bookingList.getSize()}
                    width={1000}
                    maxHeight={500}
                    {...this.props}>
                    <Column
                        fixed={true}
                        header={<Cell>Booking</Cell>}
                        cell={<TextCell data={bookingList} col="orderRef"/>}
                        width={200}
                    />
                    <Column
                        fixed={true}
                        header={<Cell>Date</Cell>}
                        cell={<TextCell data={bookingList} col="dateText"/>}
                        width={200}
                    />
                    <Column
                        fixed={true}
                        header={<Cell>Status</Cell>}
                        cell={<TextCell data={bookingList} col="status"/>}
                        width={200}
                    />
                    <Column
                        fixed={true}
                        header={<Cell>Form</Cell>}
                        cell={<LinkCell data={bookingList} col="id"/>}
                        width={150}
                    />
                    <Column
                        header={<Cell>Cancel</Cell>}
                        cell={admin && <ButtonCell
                            data={bookingList}
                            onClick={onCancel}
                            col="id"
                            status="status"/>}
                        width={admin?150:0}
                    />
                </Table>
            </div>
        );
    }
}

BookingTable.propTypes = {
    // onTodoClick: PropTypes.func.isRequired
}
module.exports = BookingTable;
