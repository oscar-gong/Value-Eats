import styled from "styled-components";
import { Link } from "react-router-dom";

export const NavLink = styled(Link)`
    padding: 9px 20px;
    font-weight: bold;
    color: #FF855B;
    transition: all 0.3s ease 0s;
    border-radius: 50px;
    margin: 10px;
    font-size: 1em;
    text-decoration: none;
    &:hover {
        background: white;
        color: #fe6b8b;
    }

    width: ${(props) => props.widthPercentage}%;
`;