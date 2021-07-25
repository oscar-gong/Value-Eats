import styled from "styled-components";
import { IconButton } from "@material-ui/core";

export const IconButtonShowSmall = styled(IconButton)`
    ${(props) => props.isDiner === "false" ? "display: none !important" : ""};
    @media (min-width: 1200px) {
        ${(props) => props.isDiner === "true" ? "display: none !important" : ""};
    }
`;
