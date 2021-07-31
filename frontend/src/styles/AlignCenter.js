import styled from "styled-components";

export const AlignCenter = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgb(255, 193, 166);
    ${props => !props.removeBg ? "background: linear-gradient(135deg, rgba(255, 193, 166, 1) 30%, rgba(224, 253, 220, 1) 75%);" : "background-color: transparent;"}
    ${props => !props.isEateryForm ? "min-height: 100vh; max-height: 100vh;" : "min-height: 0"}
`;
