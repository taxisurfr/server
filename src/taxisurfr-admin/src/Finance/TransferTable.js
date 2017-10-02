var TableStore = require('./../util/TableStore');
var FixedDataTable = require('fixed-data-table');
var React = require('react');
import {Modal,Button,FormGroup,ControlLabel,FormControl,HelpBlock} from 'react-bootstrap';
import {FormattedNumber} from 'react-intl';


const {Table, Column, ColumnGroup, Cell} = FixedDataTable;

const TextCell = ({rowIndex, data, col, ...props}) => (
    <Cell {...props}>
        <div className="mui--text-left">{data.getObjectAt(rowIndex)[col]}</div>
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



/*
const FieldGroup=({ id, label, help, ...props }) => (
        <FormGroup controlId={id}>
            <ControlLabel>{label}</ControlLabel>
            <FormControl {...props} />
            {help && <HelpBlock>{help}</HelpBlock>}
        </FormGroup>
    );
*/

class TransferTable extends React.Component {
    constructor(props) {
        super(props);
        this.saveTransfer = this.saveTransfer.bind(this);
        this.handleTransferNameChange = this.handleTransferNameChange.bind(this);

    };

    handleTransferNameChange(e){
        this.props.transferNameChange(e.target.value);
    }
    saveTransfer(){
        console.log('xxxx')
    }
    render() {
        var {transferList} = this.props;
        var {admin} = this.props;
        var {transferActive} = this.props;
        return (
            <div>
                {transferActive && <Modal.Dialog>
                    <Modal.Header>
                        <Modal.Title>Add transfer</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>
                        <form>
                            <ControlLabel>Name</ControlLabel>
                            <FormControl
                                type="text"
                                value={this.props.transferName}
                                placeholder="Enter text"
                                onChange={this.props.transferNameChange}
                            />
                            <ControlLabel>Amount $US</ControlLabel>
                            <FormControl
                                type="number"
                                value={this.props.transferAmount}
                                onChange={this.props.transferAmountChange}
                            />
                           {/* <FieldGroup
                                id="formControlsText"
                                type="text"
                                label="Name"
                                placeholder="Transfer 01.01.2002"
                            />
                            <FieldGroup
                                id="formControlsAmount"
                                type="number"
                                label="Amount USD"
                                placeholder="1000"
                            />*/}
                        </form>
                    </Modal.Body>

                    <Modal.Footer>
                        <Button onClick={() => this.props.onTransfer(false)}>Close</Button>
                        <Button bsStyle="primary"
                        onClick={() => this.props.saveTransfer(101,'some transfer description')}>
                        >Save changes</Button>
                    </Modal.Footer>

                </Modal.Dialog>}
                <row>
                    <h1>Transfers</h1>
                    {admin && <Button
                        onClick={() => this.props.onTransfer(true)}>
                        Add transfer</Button>}
                </row>
                <Table
                    rowHeight={30}
                    groupHeaderHeight={30}
                    headerHeight={30}
                    rowsCount={transferList.getSize()}
                    width={1000}
                    maxHeight={500}
                    {...this.props}>
                    <Column
                        fixed={true}
                        header={<Cell>Date</Cell>}
                        cell={<TextCell data={transferList} col="dateText"/>}
                        width={150}
                    />
                    <Column
                        fixed={true}
                        header={<Cell>Amount $US</Cell>}
                        cell={<AmtCell data={transferList} colAmt="amount"/>}
                        width={150}
                    />
                    <Column
                        fixed={true}
                        header={<Cell>Name</Cell>}
                        cell={<TextCell data={transferList} col="name"/>}
                        width={700}
                    />
                </Table>
            </div>
    );
    }
    }

    module.exports = TransferTable;
