import React, {useState} from 'react';
import {Navbar, NavbarBrand, NavbarToggler, Collapse, Nav, NavItem, NavLink} from "reactstrap";
import logo from '../../logo-normal.png';
import {performLogout, selectLogin} from "../login/loginSlice";
import {useDispatch, useSelector} from "react-redux";

export const Border = props => {
    const [collapsed, setCollapsed] = useState(true);

    const toggleNavbar = () => setCollapsed(!collapsed);

    const dispatch = useDispatch()

    const logout = e => {
        e.preventDefault()
        dispatch(performLogout())
    }

    const login = useSelector(selectLogin)

    return (
        <>
            <Navbar dark fixed="top" color="dark">
                <NavbarBrand>
                    <img src={logo} className="d-inline-block align-top" alt="" width="27" height="30" />
                    {props.title}
                </NavbarBrand>

                {props.extraNavBar}

                <NavbarToggler onClick={toggleNavbar} className="mr-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav navbar>
                        <h5 className="text-white pt-3">Logged in as {login.user.firstname} {login.user.lastname}</h5>
                        <NavItem>
                            <NavLink onClick={logout}>Logout</NavLink>
                        </NavItem>
                    </Nav>
                </Collapse>
            </Navbar>
            <div style={{paddingTop: "56px"}}>
                {props.children}
            </div>
        </>
    )
}
