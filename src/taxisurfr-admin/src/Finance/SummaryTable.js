var TableStore = require('./../util/TableStore');
var FixedDataTable = require('fixed-data-table');
var React = require('react');
import {FormattedNumber} from 'react-intl';

const {Table, Column, ColumnGroup, Cell} = FixedDataTable;

const TextCell = ({rowIndex, data, col, ...props}) => (
    <Cell {...props}>
        {data.getObjectAt(rowIndex)[col]}
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
class SummaryTable extends React.Component {
    constructor(props) {
        super(props);

    };
    render() {
        var {summaryList} = this.props;
        return (
            <div>
                <h1>Summary</h1><Table
                rowHeight={30}
                groupHeaderHeight={30}
                headerHeight={30}
                rowsCount={summaryList.getSize()}
                width={300}
                maxHeight={500}
                {...this.props}>
                <Column
                    fixed={true}
                    header={<Cell>Type</Cell>}
                    cell={<TextCell data={summaryList} col="name"/>}
                    width={150}
                />
                <Column
                    fixed={true}
                    header={<Cell>Amount $US</Cell>}
                    cell={<AmtCell data={summaryList} colAmt="amount"/>}
                    width={150}
                />
            </Table>
            </div>
        );
    }
}

module.exports = SummaryTable;
