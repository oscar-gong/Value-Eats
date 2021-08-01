import styled from "styled-components";

export const Trapezium = styled.div`
  width: 120px;
  max-height:120px;
  position: relative;
  background: #ff8a00;
  &:before {
    position: absolute;
    left: 100%;
    top: 0;
    border-right: 50px solid transparent;
    border-top: 116px solid #ff8a00;
    content: "";
  }
`;
