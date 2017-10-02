var TableStore = require('./../util/TableStore');
var FixedDataTable = require('fixed-data-table');
import React, {PropTypes} from 'react'
import Button from 'react-button';
import {FormattedNumber} from 'react-intl';

// var IntlMixin     = ReactIntl.IntlMixin;
import {FormattedDate} from 'react-intl';
/*
import {AmtCell} from './SummaryTable';
*/

const {Table, Column, ColumnGroup, Cell} = FixedDataTable;

const TextCell = ({rowIndex, data, col, ...props}) => (
    <Cell {...props}>
        {data.getObjectAt(rowIndex)[col]}
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

const AmtCell = ({rowIndex, data, colAmt, ...props}) => (
    <Cell {...props}>
        <div className="mui--text-right">
            <FormattedNumber
                value={data.getObjectAt(rowIndex)[colAmt]/100}
                style="currency"
                currency="USD" />
        </div>
    </Cell>
);
class PaymentTable extends React.Component {
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
        var {paymentList} = this.props;
        var {onCancel} = this.props;
        return (
            <div>
                <row>
                    <h1>Payments</h1>
                </row>

                <Table
                    rowHeight={30}
                    groupHeaderHeight={30}
                    headerHeight={30}
                    rowsCount={paymentList.getSize()}
                    width={1000}
                    maxHeight={500}
                    {...this.props}>
                    <Column
                        fixed={true}
                        header={<Cell>Booking</Cell>}
                        cell={<TextCell data={paymentList} col="name"/>}
                        width={200}
                    />
                    <Column
                        fixed={true}
                        header={<Cell>Date paid</Cell>}
                        cell={<DateCell data={paymentList} col="datetime"/>}
                        width={200}
                    />
                    <Column
                        fixed={true}
                        header={<Cell>Amount $US</Cell>}
                        cell={<AmtCell data={paymentList} colAmt="amount"/>}
                        width={150}
                    />
                </Table>
            </div>
        );
    }
}

PaymentTable.propTypes = {
    // onTodoClick: PropTypes.func.isRequired
}
module.exports = PaymentTable;
