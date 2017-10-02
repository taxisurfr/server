import React, {Component} from 'react';
import Request from 'react-http-request';
import Links from '../Links/Links';
import BookingTableContainer from './BookingTableContainer';
import ReactDOM from 'react-dom';
import '../../public/fixed-data-table.min.css';
import {Table, Column, Cell} from 'fixed-data-table';
const BookingHeader = () => <div><Links /><h1>Booking</h1></div>;

const
    Booking = () => <div><BookingTableContainer/></div>;



export default Booking;
