import{e as Pe,r as Te,D as R,m as V,p as De,f as se,g as $e,P as oe,i as pe,j as Oe,t as X,k as Ye,R as Qe,l as te,n as Ae,T as Ze,o as Xe,q as we,s as Je,u as xe,v as et,w as tt,x as it,E as rt,C as nt,h as Le,y as _e,z as st,A as Ie,B as ot,F as at}from"./generated-flow-imports-jDL5Otdq.js";import{g as ge,V as lt}from"./vaadin-multi-select-combo-box-BGQrisJY.js";import{d as ae,b as le,i as de,T as ke,g as dt,c as ct,e as G,f as ee,E as ht,B as ut,x as _t}from"./indexhtml-CiPQlWAv.js";/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */function U(o){return o.__cells||Array.from(o.querySelectorAll('[part~="cell"]:not([part~="details-cell"])'))}function z(o,r){[...o.children].forEach(r)}function q(o,r){U(o).forEach(r),o.__detailsCell&&r(o.__detailsCell)}function Me(o,r,e){let t=1;o.forEach(i=>{t%10===0&&(t+=1),i._order=e+t*r,t+=1})}function ce(o,r,e){switch(typeof e){case"boolean":o.toggleAttribute(r,e);break;case"string":o.setAttribute(r,e);break;default:o.removeAttribute(r);break}}function N(o,r,e){r||r===""?Pe(o,"part",e):Te(o,"part",e)}function M(o,r,e){o.forEach(t=>{N(t,e,r)})}function Z(o,r){const e=U(o);Object.entries(r).forEach(([t,i])=>{ce(o,t,i);const n=`${t}-row`;N(o,i,n),M(e,`${n}-cell`,i)})}function Ee(o,r){const e=U(o);Object.entries(r).forEach(([t,i])=>{const n=o.getAttribute(t);if(ce(o,t,i),n){const s=`${t}-${n}-row`;N(o,!1,s),M(e,`${s}-cell`,!1)}if(i){const s=`${t}-${i}-row`;N(o,i,s),M(e,`${s}-cell`,i)}})}function H(o,r,e,t,i){ce(o,r,e),i&&N(o,!1,i),N(o,e,t||`${r}-cell`)}class B{constructor(r,e){this.__host=r,this.__callback=e,this.__currentSlots=[],this.__onMutation=this.__onMutation.bind(this),this.__observer=new MutationObserver(this.__onMutation),this.__observer.observe(r,{childList:!0}),this.__initialCallDebouncer=R.debounce(this.__initialCallDebouncer,V,()=>this.__onMutation())}disconnect(){this.__observer.disconnect(),this.__initialCallDebouncer.cancel(),this.__toggleSlotChangeListeners(!1)}flush(){this.__onMutation()}__toggleSlotChangeListeners(r){this.__currentSlots.forEach(e=>{r?e.addEventListener("slotchange",this.__onMutation):e.removeEventListener("slotchange",this.__onMutation)})}__onMutation(){const r=!this.__currentColumns;this.__currentColumns||=[];const e=B.getColumns(this.__host),t=e.filter(l=>!this.__currentColumns.includes(l)),i=this.__currentColumns.filter(l=>!e.includes(l)),n=this.__currentColumns.some((l,h)=>l!==e[h]);this.__currentColumns=e,this.__toggleSlotChangeListeners(!1),this.__currentSlots=[...this.__host.children].filter(l=>l instanceof HTMLSlotElement),this.__toggleSlotChangeListeners(!0),(r||t.length||i.length||n)&&this.__callback(t,i)}static __isColumnElement(r){return r.nodeType===Node.ELEMENT_NODE&&/\bcolumn\b/u.test(r.localName)}static getColumns(r){const e=[],t=r._isColumnElement||B.__isColumnElement;return[...r.children].forEach(i=>{t(i)?e.push(i):i instanceof HTMLSlotElement&&[...i.assignedElements({flatten:!0})].filter(n=>t(n)).forEach(n=>e.push(n))}),e}}/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Be=o=>class extends o{static get properties(){return{resizable:{type:Boolean,sync:!0,value(){if(this.localName==="vaadin-grid-column-group")return;const e=this.parentNode;return e&&e.localName==="vaadin-grid-column-group"&&e.resizable||!1}},frozen:{type:Boolean,value:!1,sync:!0},frozenToEnd:{type:Boolean,value:!1,sync:!0},rowHeader:{type:Boolean,value:!1,sync:!0},hidden:{type:Boolean,value:!1,sync:!0},header:{type:String,sync:!0},textAlign:{type:String,sync:!0},headerPartName:{type:String,sync:!0},footerPartName:{type:String,sync:!0},_lastFrozen:{type:Boolean,value:!1,sync:!0},_bodyContentHidden:{type:Boolean,value:!1,sync:!0},_firstFrozenToEnd:{type:Boolean,value:!1,sync:!0},_order:{type:Number,sync:!0},_reorderStatus:{type:Boolean,sync:!0},_emptyCells:Array,_headerCell:Object,_footerCell:Object,_grid:Object,__initialized:{type:Boolean,value:!0},headerRenderer:{type:Function,sync:!0},_headerRenderer:{type:Function,computed:"_computeHeaderRenderer(headerRenderer, header, __initialized)",sync:!0},footerRenderer:{type:Function,sync:!0},_footerRenderer:{type:Function,computed:"_computeFooterRenderer(footerRenderer, __initialized)",sync:!0},__gridColumnElement:{type:Boolean,value:!0}}}static get observers(){return["_widthChanged(width, _headerCell, _footerCell, _cells)","_frozenChanged(frozen, _headerCell, _footerCell, _cells)","_frozenToEndChanged(frozenToEnd, _headerCell, _footerCell, _cells)","_flexGrowChanged(flexGrow, _headerCell, _footerCell, _cells)","_textAlignChanged(textAlign, _cells, _headerCell, _footerCell)","_orderChanged(_order, _headerCell, _footerCell, _cells)","_lastFrozenChanged(_lastFrozen)","_firstFrozenToEndChanged(_firstFrozenToEnd)","_onRendererOrBindingChanged(_renderer, _cells, _bodyContentHidden, path)","_onHeaderRendererOrBindingChanged(_headerRenderer, _headerCell, path, header)","_onFooterRendererOrBindingChanged(_footerRenderer, _footerCell)","_resizableChanged(resizable, _headerCell)","_reorderStatusChanged(_reorderStatus, _headerCell, _footerCell, _cells)","_hiddenChanged(hidden, _headerCell, _footerCell, _cells)","_rowHeaderChanged(rowHeader, _cells)","__headerFooterPartNameChanged(_headerCell, _footerCell, headerPartName, footerPartName)"]}get _grid(){return this._gridValue||(this._gridValue=this._findHostGrid()),this._gridValue}get _allCells(){return[].concat(this._cells||[]).concat(this._emptyCells||[]).concat(this._headerCell).concat(this._footerCell).filter(e=>e)}connectedCallback(){super.connectedCallback(),requestAnimationFrame(()=>{this._grid&&this._allCells.forEach(e=>{e._content.parentNode||this._grid.appendChild(e._content)})})}disconnectedCallback(){super.disconnectedCallback(),requestAnimationFrame(()=>{this._grid||this._allCells.forEach(e=>{e._content.parentNode&&e._content.parentNode.removeChild(e._content)})}),this._gridValue=void 0}ready(){super.ready(),De(this)}_findHostGrid(){let e=this;for(;e&&!/^vaadin.*grid(-pro)?$/u.test(e.localName);)e=e.assignedSlot?e.assignedSlot.parentNode:e.parentNode;return e||void 0}_renderHeaderAndFooter(){this._renderHeaderCellContent(this._headerRenderer,this._headerCell),this._renderFooterCellContent(this._footerRenderer,this._footerCell)}_flexGrowChanged(e){this.parentElement&&this.parentElement._columnPropChanged&&this.parentElement._columnPropChanged("flexGrow"),this._allCells.forEach(t=>{t.style.flexGrow=e})}_orderChanged(e){this._allCells.forEach(t=>{t.style.order=e})}_widthChanged(e){this.parentElement&&this.parentElement._columnPropChanged&&this.parentElement._columnPropChanged("width"),this._allCells.forEach(t=>{t.style.width=e})}_frozenChanged(e){this.parentElement&&this.parentElement._columnPropChanged&&this.parentElement._columnPropChanged("frozen",e),this._allCells.forEach(t=>{H(t,"frozen",e)}),this._grid&&this._grid._frozenCellsChanged&&this._grid._frozenCellsChanged()}_frozenToEndChanged(e){this.parentElement&&this.parentElement._columnPropChanged&&this.parentElement._columnPropChanged("frozenToEnd",e),this._allCells.forEach(t=>{this._grid&&t.parentElement===this._grid.$.sizer||H(t,"frozen-to-end",e)}),this._grid&&this._grid._frozenCellsChanged&&this._grid._frozenCellsChanged()}_lastFrozenChanged(e){this._allCells.forEach(t=>{H(t,"last-frozen",e)}),this.parentElement&&this.parentElement._columnPropChanged&&(this.parentElement._lastFrozen=e)}_firstFrozenToEndChanged(e){this._allCells.forEach(t=>{this._grid&&t.parentElement===this._grid.$.sizer||H(t,"first-frozen-to-end",e)}),this.parentElement&&this.parentElement._columnPropChanged&&(this.parentElement._firstFrozenToEnd=e)}_rowHeaderChanged(e,t){t&&t.forEach(i=>{i.setAttribute("role",e?"rowheader":"gridcell")})}_generateHeader(e){return e.substr(e.lastIndexOf(".")+1).replace(/([A-Z])/gu,"-$1").toLowerCase().replace(/-/gu," ").replace(/^./u,t=>t.toUpperCase())}_reorderStatusChanged(e){const t=this.__previousReorderStatus,i=t?`reorder-${t}-cell`:"",n=`reorder-${e}-cell`;this._allCells.forEach(s=>{H(s,"reorder-status",e,n,i)}),this.__previousReorderStatus=e}_resizableChanged(e,t){e===void 0||t===void 0||t&&[t].concat(this._emptyCells).forEach(i=>{if(i){const n=i.querySelector('[part~="resize-handle"]');if(n&&i.removeChild(n),e){const s=document.createElement("div");s.setAttribute("part","resize-handle"),i.appendChild(s)}}})}_textAlignChanged(e){if(e===void 0||this._grid===void 0)return;if(["start","end","center"].indexOf(e)===-1){console.warn('textAlign can only be set as "start", "end" or "center"');return}let t;getComputedStyle(this._grid).direction==="ltr"?e==="start"?t="left":e==="end"&&(t="right"):e==="start"?t="right":e==="end"&&(t="left"),this._allCells.forEach(i=>{i._content.style.textAlign=e,getComputedStyle(i._content).textAlign!==e&&(i._content.style.textAlign=t)})}_hiddenChanged(e){this.parentElement&&this.parentElement._columnPropChanged&&this.parentElement._columnPropChanged("hidden",e),!!e!=!!this._previousHidden&&this._grid&&(e===!0&&this._allCells.forEach(t=>{t._content.parentNode&&t._content.parentNode.removeChild(t._content)}),this._grid._debouncerHiddenChanged=R.debounce(this._grid._debouncerHiddenChanged,se,()=>{this._grid&&this._grid._renderColumnTree&&this._grid._renderColumnTree(this._grid._columnTree)}),this._grid._debounceUpdateFrozenColumn&&this._grid._debounceUpdateFrozenColumn(),this._grid._resetKeyboardNavigation&&this._grid._resetKeyboardNavigation()),this._previousHidden=e}_runRenderer(e,t,i){const n=i&&i.item&&!t.parentElement.hidden;if(!(n||e===this._headerRenderer||e===this._footerRenderer))return;const l=[t._content,this];n&&l.push(i),e.apply(this,l)}__renderCellsContent(e,t){this.hidden||!this._grid||t.forEach(i=>{if(!i.parentElement)return;const n=this._grid.__getRowModel(i.parentElement);e&&(i._renderer!==e&&this._clearCellContent(i),i._renderer=e,this._runRenderer(e,i,n))})}_clearCellContent(e){e._content.innerHTML="",delete e._content._$litPart$}_renderHeaderCellContent(e,t){!t||!e||(this.__renderCellsContent(e,[t]),this._grid&&t.parentElement&&this._grid.__debounceUpdateHeaderFooterRowVisibility(t.parentElement))}_onHeaderRendererOrBindingChanged(e,t,...i){this._renderHeaderCellContent(e,t)}__headerFooterPartNameChanged(e,t,i,n){[{cell:e,partName:i},{cell:t,partName:n}].forEach(({cell:s,partName:l})=>{if(s){const h=s.__customParts||[];s.part.remove(...h),s.__customParts=l?l.trim().split(" "):[],s.part.add(...s.__customParts)}})}_renderBodyCellsContent(e,t){!t||!e||this.__renderCellsContent(e,t)}_onRendererOrBindingChanged(e,t,...i){this._renderBodyCellsContent(e,t)}_renderFooterCellContent(e,t){!t||!e||(this.__renderCellsContent(e,[t]),this._grid&&t.parentElement&&this._grid.__debounceUpdateHeaderFooterRowVisibility(t.parentElement))}_onFooterRendererOrBindingChanged(e,t){this._renderFooterCellContent(e,t)}__setTextContent(e,t){e.textContent!==t&&(e.textContent=t)}__textHeaderRenderer(){this.__setTextContent(this._headerCell._content,this.header)}_defaultHeaderRenderer(){this.path&&this.__setTextContent(this._headerCell._content,this._generateHeader(this.path))}_defaultRenderer(e,t,{item:i}){this.path&&this.__setTextContent(e,ge(this.path,i))}_defaultFooterRenderer(){}_computeHeaderRenderer(e,t){return e||(t!=null?this.__textHeaderRenderer:this._defaultHeaderRenderer)}_computeRenderer(e){return e||this._defaultRenderer}_computeFooterRenderer(e){return e||this._defaultFooterRenderer}},ft=o=>class extends Be($e(o)){static get properties(){return{width:{type:String,value:"100px",sync:!0},flexGrow:{type:Number,value:1,sync:!0},renderer:{type:Function,sync:!0},_renderer:{type:Function,computed:"_computeRenderer(renderer, __initialized)",sync:!0},path:{type:String,sync:!0},autoWidth:{type:Boolean,value:!1},_focusButtonMode:{type:Boolean,value:!1},_cells:{type:Array,sync:!0}}}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */class Ne extends ft(oe){static get is(){return"vaadin-grid-column"}}ae(Ne);/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const pt=o=>class extends o{static get properties(){return{width:{type:String,value:"58px",sync:!0},autoWidth:{type:Boolean,value:!0},flexGrow:{type:Number,value:0,sync:!0},selectAll:{type:Boolean,value:!1,notify:!0,sync:!0},autoSelect:{type:Boolean,value:!1,sync:!0},dragSelect:{type:Boolean,value:!1,sync:!0},_indeterminate:{type:Boolean,sync:!0},_selectAllHidden:Boolean}}static get observers(){return["_onHeaderRendererOrBindingChanged(_headerRenderer, _headerCell, path, header, selectAll, _indeterminate, _selectAllHidden)"]}_defaultHeaderRenderer(e,t){let i=e.firstElementChild;i||(i=document.createElement("vaadin-checkbox"),i.setAttribute("aria-label","Select All"),i.classList.add("vaadin-grid-select-all-checkbox"),e.appendChild(i),i.addEventListener("checked-changed",this.__onSelectAllCheckedChanged.bind(this)));const n=this.__isChecked(this.selectAll,this._indeterminate);i.__rendererChecked=n,i.checked=n,i.hidden=this._selectAllHidden,i.indeterminate=this._indeterminate}_defaultRenderer(e,t,{item:i,selected:n}){let s=e.firstElementChild;s||(s=document.createElement("vaadin-checkbox"),s.setAttribute("aria-label","Select Row"),e.appendChild(s),s.addEventListener("checked-changed",this.__onSelectRowCheckedChanged.bind(this)),pe(e,"track",this.__onCellTrack.bind(this)),e.addEventListener("mousedown",this.__onCellMouseDown.bind(this)),e.addEventListener("click",this.__onCellClick.bind(this))),s.__item=i,s.__rendererChecked=n,s.checked=n}__onSelectAllCheckedChanged(e){e.target.checked!==e.target.__rendererChecked&&(this._indeterminate||e.target.checked?this._selectAll():this._deselectAll())}__onSelectRowCheckedChanged(e){e.target.checked!==e.target.__rendererChecked&&(e.target.checked?this._selectItem(e.target.__item):this._deselectItem(e.target.__item))}__onCellTrack(e){if(this.dragSelect)if(this.__dragCurrentY=e.detail.y,this.__dragDy=e.detail.dy,e.detail.state==="start"){const i=this._grid._getRenderedRows().find(n=>n.contains(e.currentTarget.assignedSlot));this.__selectOnDrag=!this._grid._isSelected(i._item),this.__dragStartIndex=i.index,this.__dragStartItem=i._item,this.__dragAutoScroller()}else e.detail.state==="end"&&(this.__dragStartItem&&(this.__selectOnDrag?this._selectItem(this.__dragStartItem):this._deselectItem(this.__dragStartItem)),setTimeout(()=>{this.__dragStartIndex=void 0}))}__onCellMouseDown(e){this.dragSelect&&e.preventDefault()}__onCellClick(e){this.__dragStartIndex!==void 0&&e.preventDefault()}_onCellKeyDown(e){const t=e.composedPath()[0];if(e.keyCode===32&&(t===this._headerCell||this._cells.includes(t)&&!this.autoSelect)){const i=t._content.firstElementChild;i.checked=!i.checked}}__dragAutoScroller(){if(this.__dragStartIndex===void 0)return;const e=this._grid._getRenderedRows(),t=e.find(h=>{const p=h.getBoundingClientRect();return this.__dragCurrentY>=p.top&&this.__dragCurrentY<=p.bottom});let i=t?t.index:void 0;const n=this.__getScrollableArea();this.__dragCurrentY<n.top?i=this._grid._firstVisibleIndex:this.__dragCurrentY>n.bottom&&(i=this._grid._lastVisibleIndex),i!==void 0&&e.forEach(h=>{(i>this.__dragStartIndex&&h.index>=this.__dragStartIndex&&h.index<=i||i<this.__dragStartIndex&&h.index<=this.__dragStartIndex&&h.index>=i)&&(this.__selectOnDrag?this._selectItem(h._item):this._deselectItem(h._item),this.__dragStartItem=void 0)});const s=n.height*.15,l=10;if(this.__dragDy<0&&this.__dragCurrentY<n.top+s){const h=n.top+s-this.__dragCurrentY,p=Math.min(1,h/s);this._grid.$.table.scrollTop-=p*l}if(this.__dragDy>0&&this.__dragCurrentY>n.bottom-s){const h=this.__dragCurrentY-(n.bottom-s),p=Math.min(1,h/s);this._grid.$.table.scrollTop+=p*l}setTimeout(()=>this.__dragAutoScroller(),10)}__getScrollableArea(){const e=this._grid.$.table.getBoundingClientRect(),t=this._grid.$.header.getBoundingClientRect(),i=this._grid.$.footer.getBoundingClientRect();return{top:e.top+t.height,bottom:e.bottom-i.height,left:e.left,right:e.right,height:e.height-t.height-i.height,width:e.width}}_selectAll(){}_deselectAll(){}_selectItem(e){}_deselectItem(e){}__isChecked(e,t){return t||e}};class fe extends pt(Ne){static get is(){return"vaadin-grid-flow-selection-column"}static get properties(){return{autoWidth:{type:Boolean,value:!0},width:{type:String,value:"56px"}}}_defaultHeaderRenderer(r,e){super._defaultHeaderRenderer(r,e);const t=r.firstElementChild;t&&(t.id="selectAllCheckbox")}_selectAll(){this.selectAll=!0,this.$server.selectAll()}_deselectAll(){this.selectAll=!1,this.$server.deselectAll()}_selectItem(r){this._grid.$connector.doSelection([r],!0)}_deselectItem(r){this._grid.$connector.doDeselection([r],!0),this.selectAll=!1}}customElements.define(fe.is,fe);le("vaadin-grid",de`
    :host {
      font-family: var(--lumo-font-family);
      font-size: var(--lumo-font-size-m);
      line-height: var(--lumo-line-height-s);
      color: var(--lumo-body-text-color);
      background-color: var(--lumo-base-color);
      box-sizing: border-box;
      -webkit-text-size-adjust: 100%;
      -webkit-tap-highlight-color: transparent;
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
      --_focus-ring-color: var(--vaadin-focus-ring-color, var(--lumo-primary-color-50pct));
      --_focus-ring-width: var(--vaadin-focus-ring-width, 2px);
      /* For internal use only */
      --_lumo-grid-border-color: var(--lumo-contrast-20pct);
      --_lumo-grid-secondary-border-color: var(--lumo-contrast-10pct);
      --_lumo-grid-border-width: 1px;
      --_lumo-grid-selected-row-color: var(--lumo-primary-color-10pct);
    }

    /* No (outer) border */

    :host(:not([theme~='no-border'])) {
      border: var(--_lumo-grid-border-width) solid var(--_lumo-grid-border-color);
    }

    :host([disabled]) {
      opacity: 0.7;
    }

    /* Cell styles */

    [part~='cell'] {
      min-height: var(--lumo-size-m);
      background-color: var(--vaadin-grid-cell-background, var(--lumo-base-color));
      cursor: default;
      --_cell-padding: var(--vaadin-grid-cell-padding, var(--_cell-default-padding));
      --_cell-default-padding: var(--lumo-space-xs) var(--lumo-space-m);
    }

    [part~='cell'] ::slotted(vaadin-grid-cell-content) {
      cursor: inherit;
      padding: var(--_cell-padding);
    }

    /* Apply row borders by default and introduce the "no-row-borders" variant */
    :host(:not([theme~='no-row-borders'])) [part~='cell']:not([part~='details-cell']) {
      border-top: var(--_lumo-grid-border-width) solid var(--_lumo-grid-secondary-border-color);
    }

    /* Hide first body row top border */
    :host(:not([theme~='no-row-borders'])) [part~='first-row'] [part~='cell']:not([part~='details-cell']) {
      border-top: 0;
      min-height: calc(var(--lumo-size-m) - var(--_lumo-grid-border-width));
    }

    /* Focus-ring */

    [part~='row'] {
      position: relative;
    }

    [part~='row']:focus,
    [part~='focused-cell']:focus {
      outline: none;
    }

    :host([navigating]) [part~='row']:focus::before,
    :host([navigating]) [part~='focused-cell']:focus::before {
      content: '';
      position: absolute;
      inset: 0;
      pointer-events: none;
      box-shadow: inset 0 0 0 var(--_focus-ring-width) var(--_focus-ring-color);
    }

    :host([navigating]) [part~='row']:focus::before {
      transform: translateX(calc(-1 * var(--_grid-horizontal-scroll-position)));
      z-index: 3;
    }

    /* Drag and Drop styles */
    :host([dragover])::after {
      content: '';
      position: absolute;
      z-index: 100;
      inset: 0;
      pointer-events: none;
      box-shadow: inset 0 0 0 var(--_focus-ring-width) var(--_focus-ring-color);
    }

    [part~='row'][dragover] {
      z-index: 100 !important;
    }

    [part~='row'][dragover] [part~='cell'] {
      overflow: visible;
    }

    [part~='row'][dragover] [part~='cell']::after {
      content: '';
      position: absolute;
      inset: 0;
      height: calc(var(--_lumo-grid-border-width) + 2px);
      pointer-events: none;
      background: var(--lumo-primary-color-50pct);
    }

    [part~='row'][dragover] [part~='cell'][last-frozen]::after {
      right: -1px;
    }

    :host([theme~='no-row-borders']) [dragover] [part~='cell']::after {
      height: 2px;
    }

    [part~='row'][dragover='below'] [part~='cell']::after {
      top: 100%;
      bottom: auto;
      margin-top: -1px;
    }

    :host([all-rows-visible]) [part~='last-row'][dragover='below'] [part~='cell']::after {
      height: 1px;
    }

    [part~='row'][dragover='above'] [part~='cell']::after {
      top: auto;
      bottom: 100%;
      margin-bottom: -1px;
    }

    [part~='row'][details-opened][dragover='below'] [part~='cell']:not([part~='details-cell'])::after,
    [part~='row'][details-opened][dragover='above'] [part~='details-cell']::after {
      display: none;
    }

    [part~='row'][dragover][dragover='on-top'] [part~='cell']::after {
      height: 100%;
      opacity: 0.5;
    }

    [part~='row'][dragstart] [part~='cell'] {
      border: none !important;
      box-shadow: none !important;
    }

    [part~='row'][dragstart] [part~='cell'][last-column] {
      border-radius: 0 var(--lumo-border-radius-s) var(--lumo-border-radius-s) 0;
    }

    [part~='row'][dragstart] [part~='cell'][first-column] {
      border-radius: var(--lumo-border-radius-s) 0 0 var(--lumo-border-radius-s);
    }

    #scroller [part~='row'][dragstart]:not([dragstart=''])::after {
      display: block;
      position: absolute;
      left: var(--_grid-drag-start-x);
      top: var(--_grid-drag-start-y);
      z-index: 100;
      content: attr(dragstart);
      align-items: center;
      justify-content: center;
      box-sizing: border-box;
      padding: calc(var(--lumo-space-xs) * 0.8);
      color: var(--lumo-error-contrast-color);
      background-color: var(--lumo-error-color);
      border-radius: var(--lumo-border-radius-m);
      font-family: var(--lumo-font-family);
      font-size: var(--lumo-font-size-xxs);
      line-height: 1;
      font-weight: 500;
      text-transform: initial;
      letter-spacing: initial;
      min-width: calc(var(--lumo-size-s) * 0.7);
      text-align: center;
    }

    /* Headers and footers */

    [part~='header-cell'],
    [part~='footer-cell'],
    [part~='reorder-ghost'] {
      font-size: var(--lumo-font-size-s);
      font-weight: 500;
    }

    [part~='footer-cell'] {
      font-weight: 400;
    }

    [part~='row']:only-child [part~='header-cell'] {
      min-height: var(--lumo-size-xl);
    }

    /* Header borders */

    /* Hide first header row top border */
    :host(:not([theme~='no-row-borders'])) [part~='row']:first-child [part~='header-cell'] {
      border-top: 0;
    }

    /* Hide header row top border if previous row is hidden */
    [part~='row'][hidden] + [part~='row'] [part~='header-cell'] {
      border-top: 0;
    }

    [part~='row']:last-child [part~='header-cell'] {
      border-bottom: var(--_lumo-grid-border-width) solid transparent;
    }

    :host(:not([theme~='no-row-borders'])) [part~='row']:last-child [part~='header-cell'] {
      border-bottom-color: var(--_lumo-grid-secondary-border-color);
    }

    /* Overflow uses a stronger border color */
    :host([overflow~='top']) [part~='row']:last-child [part~='header-cell'] {
      border-bottom-color: var(--_lumo-grid-border-color);
    }

    /* Footer borders */

    [part~='row']:first-child [part~='footer-cell'] {
      border-top: var(--_lumo-grid-border-width) solid transparent;
    }

    :host(:not([theme~='no-row-borders'])) [part~='row']:first-child [part~='footer-cell'] {
      border-top-color: var(--_lumo-grid-secondary-border-color);
    }

    /* Overflow uses a stronger border color */
    :host([overflow~='bottom']) [part~='row']:first-child [part~='footer-cell'] {
      border-top-color: var(--_lumo-grid-border-color);
    }

    /* Column reordering */

    :host([reordering]) [part~='cell'] {
      background: linear-gradient(var(--lumo-shade-20pct), var(--lumo-shade-20pct)) var(--lumo-base-color);
    }

    :host([reordering]) [part~='cell'][reorder-status='allowed'] {
      background: var(--lumo-base-color);
    }

    :host([reordering]) [part~='cell'][reorder-status='dragging'] {
      background: linear-gradient(var(--lumo-contrast-5pct), var(--lumo-contrast-5pct)) var(--lumo-base-color);
    }

    [part~='reorder-ghost'] {
      opacity: 0.85;
      box-shadow: var(--lumo-box-shadow-s);
      /* TODO Use the same styles as for the cell element (reorder-ghost copies styles from the cell element) */
      padding: var(--lumo-space-s) var(--lumo-space-m) !important;
    }

    /* Column resizing */

    [part='resize-handle'] {
      --_resize-handle-width: 3px;
      width: var(--_resize-handle-width);
      background-color: var(--lumo-primary-color-50pct);
      opacity: 0;
      transition: opacity 0.2s;
    }

    [part='resize-handle']::before {
      transform: translateX(calc(-50% + var(--_resize-handle-width) / 2));
      width: var(--lumo-size-s);
    }

    :host(:not([reordering])) *:not([column-resizing]) [part~='cell']:hover [part='resize-handle'],
    [part='resize-handle']:active {
      opacity: 1;
      transition-delay: 0.15s;
    }

    /* Column borders */

    :host([theme~='column-borders']) [part~='cell']:not([last-column]):not([part~='details-cell']) {
      border-right: var(--_lumo-grid-border-width) solid var(--_lumo-grid-secondary-border-color);
    }

    /* Frozen columns */

    [last-frozen] {
      border-right: var(--_lumo-grid-border-width) solid transparent;
      overflow: hidden;
    }

    :host([overflow~='start']) [part~='cell'][last-frozen]:not([part~='details-cell']) {
      border-right-color: var(--_lumo-grid-border-color);
    }

    [first-frozen-to-end] {
      border-left: var(--_lumo-grid-border-width) solid transparent;
    }

    :host([overflow~='end']) [part~='cell'][first-frozen-to-end]:not([part~='details-cell']) {
      border-left-color: var(--_lumo-grid-border-color);
    }

    /* Row stripes */

    :host([theme~='row-stripes']) [part~='even-row'] [part~='body-cell'],
    :host([theme~='row-stripes']) [part~='even-row'] [part~='details-cell'] {
      background-image: linear-gradient(var(--lumo-contrast-5pct), var(--lumo-contrast-5pct));
      background-repeat: repeat-x;
    }

    /* Selected row */

    /* Raise the selected rows above unselected rows (so that box-shadow can cover unselected rows) */
    :host(:not([reordering])) [part~='row'][selected] {
      z-index: 1;
    }

    :host(:not([reordering])) [part~='row'][selected] [part~='body-cell']:not([part~='details-cell']) {
      background-image: linear-gradient(var(--_lumo-grid-selected-row-color), var(--_lumo-grid-selected-row-color));
      background-repeat: repeat;
    }

    /* Cover the border of an unselected row */
    :host(:not([theme~='no-row-borders'])) [part~='row'][selected] [part~='cell']:not([part~='details-cell']) {
      box-shadow: 0 var(--_lumo-grid-border-width) 0 0 var(--_lumo-grid-selected-row-color);
    }

    /* Compact */

    :host([theme~='compact']) [part~='row']:only-child [part~='header-cell'] {
      min-height: var(--lumo-size-m);
    }

    :host([theme~='compact']) [part~='cell'] {
      min-height: var(--lumo-size-s);
      --_cell-default-padding: var(--lumo-space-xs) var(--lumo-space-s);
    }

    :host([theme~='compact']) [part~='first-row'] [part~='cell']:not([part~='details-cell']) {
      min-height: calc(var(--lumo-size-s) - var(--_lumo-grid-border-width));
    }

    /* Wrap cell contents */

    :host([theme~='wrap-cell-content']) [part~='cell'] ::slotted(vaadin-grid-cell-content) {
      white-space: normal;
    }

    /* RTL specific styles */

    :host([dir='rtl']) [part~='row'][dragstart] [part~='cell'][last-column] {
      border-radius: var(--lumo-border-radius-s) 0 0 var(--lumo-border-radius-s);
    }

    :host([dir='rtl']) [part~='row'][dragstart] [part~='cell'][first-column] {
      border-radius: 0 var(--lumo-border-radius-s) var(--lumo-border-radius-s) 0;
    }

    :host([dir='rtl'][theme~='column-borders']) [part~='cell']:not([last-column]):not([part~='details-cell']) {
      border-right: none;
      border-left: var(--_lumo-grid-border-width) solid var(--_lumo-grid-secondary-border-color);
    }

    :host([dir='rtl']) [last-frozen] {
      border-right: none;
      border-left: var(--_lumo-grid-border-width) solid transparent;
    }

    :host([dir='rtl']) [first-frozen-to-end] {
      border-left: none;
      border-right: var(--_lumo-grid-border-width) solid transparent;
    }

    :host([dir='rtl'][overflow~='start']) [part~='cell'][last-frozen]:not([part~='details-cell']) {
      border-left-color: var(--_lumo-grid-border-color);
    }

    :host([dir='rtl'][overflow~='end']) [part~='cell'][first-frozen-to-end]:not([part~='details-cell']) {
      border-right-color: var(--_lumo-grid-border-color);
    }
  `,{moduleId:"lumo-grid"});/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const gt=o=>class extends o{static get observers(){return["_a11yUpdateGridSize(size, _columnTree)"]}_a11yGetHeaderRowCount(e){return e.filter(t=>t.some(i=>i.headerRenderer||i.path&&i.header!==null||i.header)).length}_a11yGetFooterRowCount(e){return e.filter(t=>t.some(i=>i.headerRenderer)).length}_a11yUpdateGridSize(e,t){if(e===void 0||t===void 0)return;const i=t[t.length-1];this.$.table.setAttribute("aria-rowcount",e+this._a11yGetHeaderRowCount(t)+this._a11yGetFooterRowCount(t)),this.$.table.setAttribute("aria-colcount",i&&i.length||0),this._a11yUpdateHeaderRows(),this._a11yUpdateFooterRows()}_a11yUpdateHeaderRows(){z(this.$.header,(e,t)=>{e.setAttribute("aria-rowindex",t+1)})}_a11yUpdateFooterRows(){z(this.$.footer,(e,t)=>{e.setAttribute("aria-rowindex",this._a11yGetHeaderRowCount(this._columnTree)+this.size+t+1)})}_a11yUpdateRowRowindex(e,t){e.setAttribute("aria-rowindex",t+this._a11yGetHeaderRowCount(this._columnTree)+1)}_a11yUpdateRowSelected(e,t){e.setAttribute("aria-selected",!!t),q(e,i=>{i.setAttribute("aria-selected",!!t)})}_a11yUpdateRowExpanded(e){this.__isRowExpandable(e)?e.setAttribute("aria-expanded","false"):this.__isRowCollapsible(e)?e.setAttribute("aria-expanded","true"):e.removeAttribute("aria-expanded")}_a11yUpdateRowLevel(e,t){t>0||this.__isRowCollapsible(e)||this.__isRowExpandable(e)?e.setAttribute("aria-level",t+1):e.removeAttribute("aria-level")}_a11ySetRowDetailsCell(e,t){q(e,i=>{i!==t&&i.setAttribute("aria-controls",t.id)})}_a11yUpdateCellColspan(e,t){e.setAttribute("aria-colspan",Number(t))}_a11yUpdateSorters(){Array.from(this.querySelectorAll("vaadin-grid-sorter")).forEach(e=>{let t=e.parentNode;for(;t&&t.localName!=="vaadin-grid-cell-content";)t=t.parentNode;t&&t.assignedSlot&&t.assignedSlot.parentNode.setAttribute("aria-sort",{asc:"ascending",desc:"descending"}[String(e.direction)]||"none")})}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const He=o=>{if(!o.parentNode)return!1;const e=Array.from(o.parentNode.querySelectorAll("[tabindex], button, input, select, textarea, object, iframe, a[href], area[href]")).filter(t=>{const i=t.getAttribute("part");return!(i&&i.includes("body-cell"))}).includes(o);return!o.disabled&&e&&o.offsetParent&&getComputedStyle(o).visibility!=="hidden"},mt=o=>class extends o{static get properties(){return{activeItem:{type:Object,notify:!0,value:null,sync:!0}}}ready(){super.ready(),this.$.scroller.addEventListener("click",this._onClick.bind(this)),this.addEventListener("cell-activate",this._activateItem.bind(this)),this.addEventListener("row-activate",this._activateItem.bind(this))}_activateItem(e){const t=e.detail.model,i=t?t.item:null;i&&(this.activeItem=this._itemsEqual(this.activeItem,i)?null:i)}_shouldPreventCellActivationOnClick(e){const{cell:t}=this._getGridEventLocation(e);return e.defaultPrevented||!t||t.getAttribute("part").includes("details-cell")||t._content.contains(this.getRootNode().activeElement)||this._isFocusable(e.target)||e.target instanceof HTMLLabelElement}_onClick(e){if(this._shouldPreventCellActivationOnClick(e))return;const{cell:t}=this._getGridEventLocation(e);t&&this.dispatchEvent(new CustomEvent("cell-activate",{detail:{model:this.__getRowModel(t.parentElement)}}))}_isFocusable(e){return He(e)}};function W(o,r){return o.split(".").reduce((e,t)=>e[t],r)}function Se(o,r,e){if(e.length===0)return!1;let t=!0;return o.forEach(({path:i})=>{if(!i||i.indexOf(".")===-1)return;const n=i.replace(/\.[^.]*$/u,"");W(n,e[0])===void 0&&(console.warn(`Path "${i}" used for ${r} does not exist in all of the items, ${r} is disabled.`),t=!1)}),t}function ie(o){return[void 0,null].indexOf(o)>=0?"":isNaN(o)?o.toString():o}function Re(o,r){return o=ie(o),r=ie(r),o<r?-1:o>r?1:0}function Ct(o,r){return o.sort((e,t)=>r.map(i=>i.direction==="asc"?Re(W(i.path,e),W(i.path,t)):i.direction==="desc"?Re(W(i.path,t),W(i.path,e)):0).reduce((i,n)=>i!==0?i:n,0))}function bt(o,r){return o.filter(e=>r.every(t=>{const i=ie(W(t.path,e)),n=ie(t.value).toString().toLowerCase();return i.toString().toLowerCase().includes(n)}))}const vt=o=>(r,e)=>{let t=o?[...o]:[];r.filters&&Se(r.filters,"filtering",t)&&(t=bt(t,r.filters)),Array.isArray(r.sortOrders)&&r.sortOrders.length&&Se(r.sortOrders,"sorting",t)&&(t=Ct(t,r.sortOrders));const i=Math.min(t.length,r.pageSize),n=r.page*i,s=n+i,l=t.slice(n,s);e(l,t.length)};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const yt=o=>class extends o{static get properties(){return{items:{type:Array,sync:!0}}}static get observers(){return["__dataProviderOrItemsChanged(dataProvider, items, isAttached, items.*)"]}__setArrayDataProvider(e){const t=vt(this.items);t.__items=e,this._arrayDataProvider=t,this.size=e.length,this.dataProvider=t}_onDataProviderPageReceived(){super._onDataProviderPageReceived(),this._arrayDataProvider&&(this.size=this._flatSize)}__dataProviderOrItemsChanged(e,t,i){i&&(this._arrayDataProvider?e!==this._arrayDataProvider?(this._arrayDataProvider=void 0,this.items=void 0):t?this._arrayDataProvider.__items===t?this.clearCache():this.__setArrayDataProvider(t):(this._arrayDataProvider=void 0,this.dataProvider=void 0,this.size=0,this.clearCache()):t&&this.__setArrayDataProvider(t))}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const At=o=>class extends o{static get properties(){return{columnReorderingAllowed:{type:Boolean,value:!1},_orderBaseScope:{type:Number,value:1e7}}}static get observers(){return["_updateOrders(_columnTree)"]}ready(){super.ready(),pe(this,"track",this._onTrackEvent),this._reorderGhost=this.shadowRoot.querySelector('[part="reorder-ghost"]'),this.addEventListener("touchstart",this._onTouchStart.bind(this)),this.addEventListener("touchmove",this._onTouchMove.bind(this)),this.addEventListener("touchend",this._onTouchEnd.bind(this)),this.addEventListener("contextmenu",this._onContextMenu.bind(this))}_onContextMenu(e){this.hasAttribute("reordering")&&(e.preventDefault(),Oe||this._onTrackEnd())}_onTouchStart(e){this._startTouchReorderTimeout=setTimeout(()=>{this._onTrackStart({detail:{x:e.touches[0].clientX,y:e.touches[0].clientY}})},100)}_onTouchMove(e){this._draggedColumn&&e.preventDefault(),clearTimeout(this._startTouchReorderTimeout)}_onTouchEnd(){clearTimeout(this._startTouchReorderTimeout),this._onTrackEnd()}_onTrackEvent(e){if(e.detail.state==="start"){const t=e.composedPath(),i=t[t.indexOf(this.$.header)-2];if(!i||!i._content||i._content.contains(this.getRootNode().activeElement)||this.$.scroller.hasAttribute("column-resizing"))return;this._touchDevice||this._onTrackStart(e)}else e.detail.state==="track"?this._onTrack(e):e.detail.state==="end"&&this._onTrackEnd(e)}_onTrackStart(e){if(!this.columnReorderingAllowed)return;const t=e.composedPath&&e.composedPath();if(t&&t.some(n=>n.hasAttribute&&n.hasAttribute("draggable")))return;const i=this._cellFromPoint(e.detail.x,e.detail.y);if(!(!i||!i.getAttribute("part").includes("header-cell"))){for(this.toggleAttribute("reordering",!0),this._draggedColumn=i._column;this._draggedColumn.parentElement.childElementCount===1;)this._draggedColumn=this._draggedColumn.parentElement;this._setSiblingsReorderStatus(this._draggedColumn,"allowed"),this._draggedColumn._reorderStatus="dragging",this._updateGhost(i),this._reorderGhost.style.visibility="visible",this._updateGhostPosition(e.detail.x,this._touchDevice?e.detail.y-50:e.detail.y),this._autoScroller()}}_onTrack(e){if(!this._draggedColumn)return;const t=this._cellFromPoint(e.detail.x,e.detail.y);if(!t)return;const i=this._getTargetColumn(t,this._draggedColumn);if(this._isSwapAllowed(this._draggedColumn,i)&&this._isSwappableByPosition(i,e.detail.x)){const n=this._columnTree.findIndex(_=>_.includes(i)),s=this._getColumnsInOrder(n),l=s.indexOf(this._draggedColumn),h=s.indexOf(i),p=l<h?1:-1;for(let _=l;_!==h;_+=p)this._swapColumnOrders(this._draggedColumn,s[_+p])}this._updateGhostPosition(e.detail.x,this._touchDevice?e.detail.y-50:e.detail.y),this._lastDragClientX=e.detail.x}_onTrackEnd(){this._draggedColumn&&(this.toggleAttribute("reordering",!1),this._draggedColumn._reorderStatus="",this._setSiblingsReorderStatus(this._draggedColumn,""),this._draggedColumn=null,this._lastDragClientX=null,this._reorderGhost.style.visibility="hidden",this.dispatchEvent(new CustomEvent("column-reorder",{detail:{columns:this._getColumnsInOrder()}})))}_getColumnsInOrder(e=this._columnTree.length-1){return this._columnTree[e].filter(t=>!t.hidden).sort((t,i)=>t._order-i._order)}_cellFromPoint(e=0,t=0){this._draggedColumn||this.$.scroller.toggleAttribute("no-content-pointer-events",!0);const i=this.shadowRoot.elementFromPoint(e,t);return this.$.scroller.toggleAttribute("no-content-pointer-events",!1),this._getCellFromElement(i)}_getCellFromElement(e){if(e){if(e._column)return e;const{parentElement:t}=e;if(t&&t._focusButton===e)return t}return null}_updateGhostPosition(e,t){const i=this._reorderGhost.getBoundingClientRect(),n=e-i.width/2,s=t-i.height/2,l=parseInt(this._reorderGhost._left||0),h=parseInt(this._reorderGhost._top||0);this._reorderGhost._left=l-(i.left-n),this._reorderGhost._top=h-(i.top-s),this._reorderGhost.style.transform=`translate(${this._reorderGhost._left}px, ${this._reorderGhost._top}px)`}_updateGhost(e){const t=this._reorderGhost;t.textContent=e._content.innerText;const i=window.getComputedStyle(e);return["boxSizing","display","width","height","background","alignItems","padding","border","flex-direction","overflow"].forEach(n=>{t.style[n]=i[n]}),t}_updateOrders(e){e!==void 0&&(e[0].forEach(t=>{t._order=0}),Me(e[0],this._orderBaseScope,0))}_setSiblingsReorderStatus(e,t){z(e.parentNode,i=>{/column/u.test(i.localName)&&this._isSwapAllowed(i,e)&&(i._reorderStatus=t)})}_autoScroller(){if(this._lastDragClientX){const e=this._lastDragClientX-this.getBoundingClientRect().right+50,t=this.getBoundingClientRect().left-this._lastDragClientX+50;e>0?this.$.table.scrollLeft+=e/10:t>0&&(this.$.table.scrollLeft-=t/10)}this._draggedColumn&&setTimeout(()=>this._autoScroller(),10)}_isSwapAllowed(e,t){if(e&&t){const i=e!==t,n=e.parentElement===t.parentElement,s=e.frozen&&t.frozen||e.frozenToEnd&&t.frozenToEnd||!e.frozen&&!e.frozenToEnd&&!t.frozen&&!t.frozenToEnd;return i&&n&&s}}_isSwappableByPosition(e,t){const i=Array.from(this.$.header.querySelectorAll('tr:not([hidden]) [part~="cell"]')).find(l=>e.contains(l._column)),n=this.$.header.querySelector("tr:not([hidden]) [reorder-status=dragging]").getBoundingClientRect(),s=i.getBoundingClientRect();return s.left>n.left?t>s.right-n.width:t<s.left+n.width}_swapColumnOrders(e,t){[e._order,t._order]=[t._order,e._order],this._debounceUpdateFrozenColumn(),this._updateFirstAndLastColumn()}_getTargetColumn(e,t){if(e&&t){let i=e._column;for(;i.parentElement!==t.parentElement&&i!==this;)i=i.parentElement;return i.parentElement===t.parentElement?i:e._column}}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const wt=o=>class extends o{ready(){super.ready();const e=this.$.scroller;pe(e,"track",this._onHeaderTrack.bind(this)),e.addEventListener("touchmove",t=>e.hasAttribute("column-resizing")&&t.preventDefault()),e.addEventListener("contextmenu",t=>t.target.getAttribute("part")==="resize-handle"&&t.preventDefault()),e.addEventListener("mousedown",t=>t.target.getAttribute("part")==="resize-handle"&&t.preventDefault())}_onHeaderTrack(e){const t=e.target;if(t.getAttribute("part")==="resize-handle"){let n=t.parentElement._column;for(this.$.scroller.toggleAttribute("column-resizing",!0);n.localName==="vaadin-grid-column-group";)n=n._childColumns.slice(0).sort((m,g)=>m._order-g._order).filter(m=>!m.hidden).pop();const s=this.__isRTL,l=e.detail.x,h=Array.from(this.$.header.querySelectorAll('[part~="row"]:last-child [part~="cell"]')),p=h.find(m=>m._column===n);if(p.offsetWidth){const m=getComputedStyle(p._content),g=10+parseInt(m.paddingLeft)+parseInt(m.paddingRight)+parseInt(m.borderLeftWidth)+parseInt(m.borderRightWidth)+parseInt(m.marginLeft)+parseInt(m.marginRight);let b;const v=p.offsetWidth,S=p.getBoundingClientRect();p.hasAttribute("frozen-to-end")?b=v+(s?l-S.right:S.left-l):b=v+(s?S.left-l:l-S.right),n.width=`${Math.max(g,b)}px`,n.flexGrow=0}h.sort((m,g)=>m._column._order-g._column._order).forEach((m,g,b)=>{g<b.indexOf(p)&&(m._column.width=`${m.offsetWidth}px`,m._column.flexGrow=0)});const _=this._frozenToEndCells[0];if(_&&this.$.table.scrollWidth>this.$.table.offsetWidth){const m=_.getBoundingClientRect(),g=l-(s?m.right:m.left);(s&&g<=0||!s&&g>=0)&&(this.$.table.scrollLeft+=g)}e.detail.state==="end"&&(this.$.scroller.toggleAttribute("column-resizing",!1),this.dispatchEvent(new CustomEvent("column-resize",{detail:{resizedColumn:n}}))),this._resizeHandler()}}};/**
 * @license
 * Copyright (c) 2021 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */function re(o,r,e=0){let t=r;for(const i of o.subCaches){const n=i.parentCacheIndex;if(t<=n)break;if(t<=n+i.flatSize)return re(i,t-n-1,e+1);t-=i.flatSize}return{cache:o,item:o.items[t],index:t,page:Math.floor(t/o.pageSize),level:e}}function Ge({getItemId:o},r,e,t=0,i=0){for(let n=0;n<r.items.length;n++){const s=r.items[n];if(s&&o(s)===o(e))return{cache:r,level:t,item:s,index:n,page:Math.floor(n/r.pageSize),subCache:r.getSubCache(n),flatIndex:i+r.getFlatIndex(n)}}for(const n of r.subCaches){const s=i+r.getFlatIndex(n.parentCacheIndex),l=Ge({getItemId:o},n,e,t+1,s+1);if(l)return l}}function We(o,[r,...e],t=0){r===1/0&&(r=o.size-1);const i=o.getFlatIndex(r),n=o.getSubCache(r);return n&&n.flatSize>0&&e.length?We(n,e,t+i+1):t+i}/**
 * @license
 * Copyright (c) 2021 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */class me{context;size=0;pageSize;items=[];pendingRequests={};__subCacheByIndex={};__flatSize=0;constructor(r,e,t,i,n){this.context=r,this.pageSize=e,this.size=t||0,this.parentCache=i,this.parentCacheIndex=n,this.__flatSize=t||0}get parentItem(){return this.parentCache&&this.parentCache.items[this.parentCacheIndex]}get subCaches(){return Object.values(this.__subCacheByIndex)}get isLoading(){return Object.keys(this.pendingRequests).length>0?!0:this.subCaches.some(r=>r.isLoading)}get flatSize(){return this.__flatSize}get effectiveSize(){return console.warn("<vaadin-grid> The `effectiveSize` property of ItemCache is deprecated and will be removed in Vaadin 25."),this.flatSize}recalculateFlatSize(){this.__flatSize=!this.parentItem||this.context.isExpanded(this.parentItem)?this.size+this.subCaches.reduce((r,e)=>(e.recalculateFlatSize(),r+e.flatSize),0):0}setPage(r,e){const t=r*this.pageSize;e.forEach((i,n)=>{this.items[t+n]=i})}getSubCache(r){return this.__subCacheByIndex[r]}removeSubCache(r){delete this.__subCacheByIndex[r]}removeSubCaches(){this.__subCacheByIndex={}}createSubCache(r){const e=new me(this.context,this.pageSize,0,this,r);return this.__subCacheByIndex[r]=e,e}getFlatIndex(r){const e=Math.max(0,Math.min(this.size-1,r));return this.subCaches.reduce((t,i)=>{const n=i.parentCacheIndex;return e>n?t+i.flatSize:t},e)}getItemForIndex(r){console.warn("<vaadin-grid> The `getItemForIndex` method of ItemCache is deprecated and will be removed in Vaadin 25.");const{item:e}=re(this,r);return e}getCacheAndIndex(r){console.warn("<vaadin-grid> The `getCacheAndIndex` method of ItemCache is deprecated and will be removed in Vaadin 25.");const{cache:e,index:t}=re(this,r);return{cache:e,scaledIndex:t}}updateSize(){console.warn("<vaadin-grid> The `updateSize` method of ItemCache is deprecated and will be removed in Vaadin 25."),this.recalculateFlatSize()}ensureSubCacheForScaledIndex(r){if(console.warn("<vaadin-grid> The `ensureSubCacheForScaledIndex` method of ItemCache is deprecated and will be removed in Vaadin 25."),!this.getSubCache(r)){const e=this.createSubCache(r);this.context.__controller.__loadCachePage(e,0)}}get grid(){return console.warn("<vaadin-grid> The `grid` property of ItemCache is deprecated and will be removed in Vaadin 25."),this.context.__controller.host}get itemCaches(){return console.warn("<vaadin-grid> The `itemCaches` property of ItemCache is deprecated and will be removed in Vaadin 25."),this.__subCacheByIndex}}/**
 * @license
 * Copyright (c) 2021 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */class xt extends EventTarget{host;dataProvider;dataProviderParams;size;pageSize;isExpanded;getItemId;rootCache;constructor(r,{size:e,pageSize:t,isExpanded:i,getItemId:n,dataProvider:s,dataProviderParams:l}){super(),this.host=r,this.pageSize=t,this.getItemId=n,this.isExpanded=i,this.dataProvider=s,this.dataProviderParams=l,this.rootCache=this.__createRootCache(e)}get flatSize(){return this.rootCache.flatSize}get __cacheContext(){return{isExpanded:this.isExpanded,__controller:this}}isLoading(){return this.rootCache.isLoading}setPageSize(r){this.pageSize=r,this.clearCache()}setDataProvider(r){this.dataProvider=r,this.clearCache()}recalculateFlatSize(){this.rootCache.recalculateFlatSize()}clearCache(){this.rootCache=this.__createRootCache(this.rootCache.size)}getFlatIndexContext(r){return re(this.rootCache,r)}getItemContext(r){return Ge({getItemId:this.getItemId},this.rootCache,r)}getFlatIndexByPath(r){return We(this.rootCache,r)}ensureFlatIndexLoaded(r){const{cache:e,page:t,item:i}=this.getFlatIndexContext(r);i||this.__loadCachePage(e,t)}ensureFlatIndexHierarchy(r){const{cache:e,item:t,index:i}=this.getFlatIndexContext(r);if(t&&this.isExpanded(t)&&!e.getSubCache(i)){const n=e.createSubCache(i);this.__loadCachePage(n,0)}}loadFirstPage(){this.__loadCachePage(this.rootCache,0)}__createRootCache(r){return new me(this.__cacheContext,this.pageSize,r)}__loadCachePage(r,e){if(!this.dataProvider||r.pendingRequests[e])return;let t={page:e,pageSize:this.pageSize,parentItem:r.parentItem};this.dataProviderParams&&(t={...t,...this.dataProviderParams()});const i=(n,s)=>{r.pendingRequests[e]===i&&(s!==void 0?r.size=s:t.parentItem&&(r.size=n.length),r.setPage(e,n),this.recalculateFlatSize(),this.dispatchEvent(new CustomEvent("page-received")),delete r.pendingRequests[e],this.dispatchEvent(new CustomEvent("page-loaded")))};r.pendingRequests[e]=i,this.dispatchEvent(new CustomEvent("page-requested")),this.dataProvider(t,i)}}/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const It=o=>class extends o{static get properties(){return{size:{type:Number,notify:!0,sync:!0},_flatSize:{type:Number,sync:!0},pageSize:{type:Number,value:50,observer:"_pageSizeChanged",sync:!0},dataProvider:{type:Object,notify:!0,observer:"_dataProviderChanged",sync:!0},loading:{type:Boolean,notify:!0,readOnly:!0,reflectToAttribute:!0},_hasData:{type:Boolean,value:!1,sync:!0},itemHasChildrenPath:{type:String,value:"children",observer:"__itemHasChildrenPathChanged",sync:!0},itemIdPath:{type:String,value:null,sync:!0},expandedItems:{type:Object,notify:!0,value:()=>[],sync:!0},__expandedKeys:{type:Object,computed:"__computeExpandedKeys(itemIdPath, expandedItems)"}}}static get observers(){return["_sizeChanged(size)","_expandedItemsChanged(expandedItems)"]}constructor(){super(),this._dataProviderController=new xt(this,{size:this.size,pageSize:this.pageSize,getItemId:this.getItemId.bind(this),isExpanded:this._isExpanded.bind(this),dataProvider:this.dataProvider?this.dataProvider.bind(this):null,dataProviderParams:()=>({sortOrders:this._mapSorters(),filters:this._mapFilters()})}),this._dataProviderController.addEventListener("page-requested",this._onDataProviderPageRequested.bind(this)),this._dataProviderController.addEventListener("page-received",this._onDataProviderPageReceived.bind(this)),this._dataProviderController.addEventListener("page-loaded",this._onDataProviderPageLoaded.bind(this))}get _cache(){return console.warn("<vaadin-grid> The `_cache` property is deprecated and will be removed in Vaadin 25."),this._dataProviderController.rootCache}get _effectiveSize(){return console.warn("<vaadin-grid> The `_effectiveSize` property is deprecated and will be removed in Vaadin 25."),this._flatSize}_sizeChanged(e){this._dataProviderController.rootCache.size=e,this._dataProviderController.recalculateFlatSize(),this._flatSize=this._dataProviderController.flatSize}__itemHasChildrenPathChanged(e,t){!t&&e==="children"||this.requestContentUpdate()}_getItem(e,t){t.index=e;const{item:i}=this._dataProviderController.getFlatIndexContext(e);i?(this.__updateLoading(t,!1),this._updateItem(t,i),this._isExpanded(i)&&this._dataProviderController.ensureFlatIndexHierarchy(e)):(this.__updateLoading(t,!0),this._dataProviderController.ensureFlatIndexLoaded(e))}__updateLoading(e,t){const i=U(e);ce(e,"loading",t),M(i,"loading-row-cell",t),t&&(this._generateCellClassNames(e),this._generateCellPartNames(e))}getItemId(e){return this.itemIdPath?ge(this.itemIdPath,e):e}_isExpanded(e){return this.__expandedKeys&&this.__expandedKeys.has(this.getItemId(e))}_expandedItemsChanged(){this._dataProviderController.recalculateFlatSize(),this._flatSize=this._dataProviderController.flatSize,this.__updateVisibleRows()}__computeExpandedKeys(e,t){const i=t||[],n=new Set;return i.forEach(s=>{n.add(this.getItemId(s))}),n}expandItem(e){this._isExpanded(e)||(this.expandedItems=[...this.expandedItems,e])}collapseItem(e){this._isExpanded(e)&&(this.expandedItems=this.expandedItems.filter(t=>!this._itemsEqual(t,e)))}_getIndexLevel(e=0){const{level:t}=this._dataProviderController.getFlatIndexContext(e);return t}_loadPage(e,t){console.warn("<vaadin-grid> The `_loadPage` method is deprecated and will be removed in Vaadin 25."),this._dataProviderController.__loadCachePage(t,e)}_onDataProviderPageRequested(){this._setLoading(!0)}_onDataProviderPageReceived(){this._flatSize!==this._dataProviderController.flatSize&&(this._shouldUpdateAllRenderedRowsAfterPageLoad=!0,this._flatSize=this._dataProviderController.flatSize),this._getRenderedRows().forEach(e=>{this._dataProviderController.ensureFlatIndexHierarchy(e.index)}),this._hasData=!0}_onDataProviderPageLoaded(){this._debouncerApplyCachedData=R.debounce(this._debouncerApplyCachedData,X.after(0),()=>{this._setLoading(!1);const e=this._shouldUpdateAllRenderedRowsAfterPageLoad;this._shouldUpdateAllRenderedRowsAfterPageLoad=!1,this._getRenderedRows().forEach(t=>{const{item:i}=this._dataProviderController.getFlatIndexContext(t.index);(i||e)&&this._getItem(t.index,t)}),this.__scrollToPendingIndexes(),this.__dispatchPendingBodyCellFocus()}),this._dataProviderController.isLoading()||this._debouncerApplyCachedData.flush()}__debounceClearCache(){this.__clearCacheDebouncer=R.debounce(this.__clearCacheDebouncer,V,()=>this.clearCache())}clearCache(){this._dataProviderController.clearCache(),this._dataProviderController.rootCache.size=this.size,this._dataProviderController.recalculateFlatSize(),this._hasData=!1,this.__updateVisibleRows(),(!this.__virtualizer||!this.__virtualizer.size)&&this._dataProviderController.loadFirstPage()}_pageSizeChanged(e,t){this._dataProviderController.setPageSize(e),t!==void 0&&e!==t&&this.clearCache()}_checkSize(){this.size===void 0&&this._flatSize===0&&console.warn("The <vaadin-grid> needs the total number of items in order to display rows, which you can specify either by setting the `size` property, or by providing it to the second argument of the `dataProvider` function `callback` call.")}_dataProviderChanged(e,t){this._dataProviderController.setDataProvider(e?e.bind(this):null),t!==void 0&&this.clearCache(),this._ensureFirstPageLoaded(),this._debouncerCheckSize=R.debounce(this._debouncerCheckSize,X.after(2e3),this._checkSize.bind(this))}_ensureFirstPageLoaded(){this._hasData||this._dataProviderController.loadFirstPage()}_itemsEqual(e,t){return this.getItemId(e)===this.getItemId(t)}_getItemIndexInArray(e,t){let i=-1;return t.forEach((n,s)=>{this._itemsEqual(n,e)&&(i=s)}),i}scrollToIndex(...e){let t;for(;t!==(t=this._dataProviderController.getFlatIndexByPath(e));)this._scrollToFlatIndex(t);(this._dataProviderController.isLoading()||!this.clientHeight)&&(this.__pendingScrollToIndexes=e)}__scrollToPendingIndexes(){if(this.__pendingScrollToIndexes&&this.$.items.children.length){const e=this.__pendingScrollToIndexes;delete this.__pendingScrollToIndexes,this.scrollToIndex(...e)}}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Y={BETWEEN:"between",ON_TOP:"on-top",ON_TOP_OR_BETWEEN:"on-top-or-between",ON_GRID:"on-grid"},k={ON_TOP:"on-top",ABOVE:"above",BELOW:"below",EMPTY:"empty"},Et=!("draggable"in document.createElement("div")),St=o=>class extends o{static get properties(){return{dropMode:{type:String,sync:!0},rowsDraggable:{type:Boolean,sync:!0},dragFilter:{type:Function,sync:!0},dropFilter:{type:Function,sync:!0},__dndAutoScrollThreshold:{value:50}}}static get observers(){return["_dragDropAccessChanged(rowsDraggable, dropMode, dragFilter, dropFilter, loading)"]}ready(){super.ready(),this.$.table.addEventListener("dragstart",this._onDragStart.bind(this)),this.$.table.addEventListener("dragend",this._onDragEnd.bind(this)),this.$.table.addEventListener("dragover",this._onDragOver.bind(this)),this.$.table.addEventListener("dragleave",this._onDragLeave.bind(this)),this.$.table.addEventListener("drop",this._onDrop.bind(this)),this.$.table.addEventListener("dragenter",e=>{this.dropMode&&(e.preventDefault(),e.stopPropagation())})}_onDragStart(e){if(this.rowsDraggable){let t=e.target;if(t.localName==="vaadin-grid-cell-content"&&(t=t.assignedSlot.parentNode.parentNode),t.parentNode!==this.$.items)return;if(e.stopPropagation(),this.toggleAttribute("dragging-rows",!0),this._safari){const l=t.style.transform;t.style.top=/translateY\((.*)\)/u.exec(l)[1],t.style.transform="none",requestAnimationFrame(()=>{t.style.top="",t.style.transform=l})}const i=t.getBoundingClientRect();Et?e.dataTransfer.setDragImage(t):e.dataTransfer.setDragImage(t,e.clientX-i.left,e.clientY-i.top);let n=[t];this._isSelected(t._item)&&(n=this.__getViewportRows().filter(l=>this._isSelected(l._item)).filter(l=>!this.dragFilter||this.dragFilter(this.__getRowModel(l)))),e.dataTransfer.setData("text",this.__formatDefaultTransferData(n)),Z(t,{dragstart:n.length>1?`${n.length}`:""}),this.style.setProperty("--_grid-drag-start-x",`${e.clientX-i.left+20}px`),this.style.setProperty("--_grid-drag-start-y",`${e.clientY-i.top+10}px`),requestAnimationFrame(()=>{Z(t,{dragstart:!1}),this.style.setProperty("--_grid-drag-start-x",""),this.style.setProperty("--_grid-drag-start-y","")});const s=new CustomEvent("grid-dragstart",{detail:{draggedItems:n.map(l=>l._item),setDragData:(l,h)=>e.dataTransfer.setData(l,h),setDraggedItemsCount:l=>t.setAttribute("dragstart",l)}});s.originalEvent=e,this.dispatchEvent(s)}}_onDragEnd(e){this.toggleAttribute("dragging-rows",!1),e.stopPropagation();const t=new CustomEvent("grid-dragend");t.originalEvent=e,this.dispatchEvent(t)}_onDragLeave(e){e.stopPropagation(),this._clearDragStyles()}_onDragOver(e){if(this.dropMode){if(this._dropLocation=void 0,this._dragOverItem=void 0,this.__dndAutoScroll(e.clientY)){this._clearDragStyles();return}let t=e.composedPath().find(i=>i.localName==="tr");if(!this._flatSize||this.dropMode===Y.ON_GRID)this._dropLocation=k.EMPTY;else if(!t||t.parentNode!==this.$.items){if(t)return;if(this.dropMode===Y.BETWEEN||this.dropMode===Y.ON_TOP_OR_BETWEEN)t=Array.from(this.$.items.children).filter(i=>!i.hidden).pop(),this._dropLocation=k.BELOW;else return}else{const i=t.getBoundingClientRect();if(this._dropLocation=k.ON_TOP,this.dropMode===Y.BETWEEN){const n=e.clientY-i.top<i.bottom-e.clientY;this._dropLocation=n?k.ABOVE:k.BELOW}else this.dropMode===Y.ON_TOP_OR_BETWEEN&&(e.clientY-i.top<i.height/3?this._dropLocation=k.ABOVE:e.clientY-i.top>i.height/3*2&&(this._dropLocation=k.BELOW))}if(t&&t.hasAttribute("drop-disabled")){this._dropLocation=void 0;return}e.stopPropagation(),e.preventDefault(),this._dropLocation===k.EMPTY?this.toggleAttribute("dragover",!0):t?(this._dragOverItem=t._item,t.getAttribute("dragover")!==this._dropLocation&&Ee(t,{dragover:this._dropLocation})):this._clearDragStyles()}}__dndAutoScroll(e){if(this.__dndAutoScrolling)return!0;const t=this.$.header.getBoundingClientRect().bottom,i=this.$.footer.getBoundingClientRect().top,n=t-e+this.__dndAutoScrollThreshold,s=e-i+this.__dndAutoScrollThreshold;let l=0;if(s>0?l=s*2:n>0&&(l=-n*2),l){const h=this.$.table.scrollTop;if(this.$.table.scrollTop+=l,h!==this.$.table.scrollTop)return this.__dndAutoScrolling=!0,setTimeout(()=>{this.__dndAutoScrolling=!1},20),!0}}__getViewportRows(){const e=this.$.header.getBoundingClientRect().bottom,t=this.$.footer.getBoundingClientRect().top;return Array.from(this.$.items.children).filter(i=>{const n=i.getBoundingClientRect();return n.bottom>e&&n.top<t})}_clearDragStyles(){this.removeAttribute("dragover"),z(this.$.items,e=>{Ee(e,{dragover:null})})}_onDrop(e){if(this.dropMode){e.stopPropagation(),e.preventDefault();const t=e.dataTransfer.types&&Array.from(e.dataTransfer.types).map(n=>({type:n,data:e.dataTransfer.getData(n)}));this._clearDragStyles();const i=new CustomEvent("grid-drop",{bubbles:e.bubbles,cancelable:e.cancelable,detail:{dropTargetItem:this._dragOverItem,dropLocation:this._dropLocation,dragData:t}});i.originalEvent=e,this.dispatchEvent(i)}}__formatDefaultTransferData(e){return e.map(t=>Array.from(t.children).filter(i=>!i.hidden&&i.getAttribute("part").indexOf("details-cell")===-1).sort((i,n)=>i._column._order>n._column._order?1:-1).map(i=>i._content.textContent.trim()).filter(i=>i).join("	")).join(`
`)}_dragDropAccessChanged(){this.filterDragAndDrop()}filterDragAndDrop(){z(this.$.items,e=>{e.hidden||this._filterDragAndDrop(e,this.__getRowModel(e))})}_filterDragAndDrop(e,t){const i=this.loading||e.hasAttribute("loading"),n=!this.rowsDraggable||i||this.dragFilter&&!this.dragFilter(t),s=!this.dropMode||i||this.dropFilter&&!this.dropFilter(t);q(e,l=>{n?l._content.removeAttribute("draggable"):l._content.setAttribute("draggable",!0)}),Z(e,{"drag-disabled":!!n,"drop-disabled":!!s})}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */function Ve(o,r){if(!o||!r||o.length!==r.length)return!1;for(let e=0,t=o.length;e<t;e++)if(o[e]instanceof Array&&r[e]instanceof Array){if(!Ve(o[e],r[e]))return!1}else if(o[e]!==r[e])return!1;return!0}const Rt=o=>class extends o{static get properties(){return{_columnTree:Object}}ready(){super.ready(),this._addNodeObserver()}_hasColumnGroups(e){return e.some(t=>t.localName==="vaadin-grid-column-group")}_getChildColumns(e){return B.getColumns(e)}_flattenColumnGroups(e){return e.map(t=>t.localName==="vaadin-grid-column-group"?this._getChildColumns(t):[t]).reduce((t,i)=>t.concat(i),[])}_getColumnTree(){const e=B.getColumns(this),t=[e];let i=e;for(;this._hasColumnGroups(i);)i=this._flattenColumnGroups(i),t.push(i);return t}_debounceUpdateColumnTree(){this.__updateColumnTreeDebouncer=R.debounce(this.__updateColumnTreeDebouncer,V,()=>this._updateColumnTree())}_updateColumnTree(){const e=this._getColumnTree();Ve(e,this._columnTree)||(e.forEach(t=>{t.forEach(i=>{i.performUpdate&&i.performUpdate()})}),this._columnTree=e)}_addNodeObserver(){this._observer=new B(this,(e,t)=>{const i=t.flatMap(s=>s._allCells),n=s=>i.filter(l=>l&&l._content.contains(s)).length;this.__removeSorters(this._sorters.filter(n)),this.__removeFilters(this._filters.filter(n)),this._debounceUpdateColumnTree(),this._debouncerCheckImports=R.debounce(this._debouncerCheckImports,X.after(2e3),this._checkImports.bind(this)),this._ensureFirstPageLoaded()})}_checkImports(){["vaadin-grid-column-group","vaadin-grid-filter","vaadin-grid-filter-column","vaadin-grid-tree-toggle","vaadin-grid-selection-column","vaadin-grid-sort-column","vaadin-grid-sorter"].forEach(e=>{this.querySelector(e)&&!customElements.get(e)&&console.warn(`Make sure you have imported the required module for <${e}> element.`)})}_updateFirstAndLastColumn(){Array.from(this.shadowRoot.querySelectorAll("tr")).forEach(e=>this._updateFirstAndLastColumnForRow(e))}_updateFirstAndLastColumnForRow(e){Array.from(e.querySelectorAll('[part~="cell"]:not([part~="details-cell"])')).sort((t,i)=>t._column._order-i._column._order).forEach((t,i,n)=>{H(t,"first-column",i===0),H(t,"last-column",i===n.length-1)})}_isColumnElement(e){return e.nodeType===Node.ELEMENT_NODE&&/\bcolumn\b/u.test(e.localName)}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const zt=o=>class extends o{getEventContext(e){const t={},i=e.__composedPath||e.composedPath(),n=i[i.indexOf(this.$.table)-3];return n&&(t.section=["body","header","footer","details"].find(s=>n.getAttribute("part").indexOf(s)>-1),n._column&&(t.column=n._column),(t.section==="body"||t.section==="details")&&Object.assign(t,this.__getRowModel(n.parentElement))),t}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Ft=o=>class extends o{static get properties(){return{_filters:{type:Array,value:()=>[]}}}constructor(){super(),this._filterChanged=this._filterChanged.bind(this),this.addEventListener("filter-changed",this._filterChanged)}_filterChanged(e){e.stopPropagation(),this.__addFilter(e.target),this.__applyFilters()}__removeFilters(e){e.length!==0&&(this._filters=this._filters.filter(t=>e.indexOf(t)<0),this.__applyFilters())}__addFilter(e){this._filters.indexOf(e)===-1&&this._filters.push(e)}__applyFilters(){this.dataProvider&&this.isAttached&&this.clearCache()}_mapFilters(){return this._filters.map(e=>({path:e.path,value:e.value}))}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Pt=o=>class extends o{static get properties(){return{_headerFocusable:{type:Object,observer:"_focusableChanged",sync:!0},_itemsFocusable:{type:Object,observer:"_focusableChanged",sync:!0},_footerFocusable:{type:Object,observer:"_focusableChanged",sync:!0},_navigatingIsHidden:Boolean,_focusedItemIndex:{type:Number,value:0},_focusedColumnOrder:Number,_focusedCell:{type:Object,observer:"_focusedCellChanged",sync:!0},interacting:{type:Boolean,value:!1,reflectToAttribute:!0,readOnly:!0,observer:"_interactingChanged"}}}get __rowFocusMode(){return this.__isRow(this._itemsFocusable)||this.__isRow(this._headerFocusable)||this.__isRow(this._footerFocusable)}set __rowFocusMode(e){["_itemsFocusable","_footerFocusable","_headerFocusable"].forEach(t=>{const i=this[t];if(e){const n=i&&i.parentElement;this.__isCell(i)?this[t]=n:this.__isCell(n)&&(this[t]=n.parentElement)}else if(!e&&this.__isRow(i)){const n=i.firstElementChild;this[t]=n._focusButton||n}})}get _visibleItemsCount(){return this._lastVisibleIndex-this._firstVisibleIndex-1}ready(){super.ready(),!(this._ios||this._android)&&(this.addEventListener("keydown",this._onKeyDown),this.addEventListener("keyup",this._onKeyUp),this.addEventListener("focusin",this._onFocusIn),this.addEventListener("focusout",this._onFocusOut),this.$.table.addEventListener("focusin",this._onContentFocusIn.bind(this)),this.addEventListener("mousedown",()=>{this.toggleAttribute("navigating",!1),this._isMousedown=!0,this._focusedColumnOrder=void 0}),this.addEventListener("mouseup",()=>{this._isMousedown=!1}))}_focusableChanged(e,t){t&&t.setAttribute("tabindex","-1"),e&&this._updateGridSectionFocusTarget(e)}_focusedCellChanged(e,t){t&&Te(t,"part","focused-cell"),e&&Pe(e,"part","focused-cell")}_interactingChanged(){this._updateGridSectionFocusTarget(this._headerFocusable),this._updateGridSectionFocusTarget(this._itemsFocusable),this._updateGridSectionFocusTarget(this._footerFocusable)}__updateItemsFocusable(){if(!this._itemsFocusable)return;const e=this.shadowRoot.activeElement===this._itemsFocusable;this._getRenderedRows().forEach(t=>{if(t.index===this._focusedItemIndex)if(this.__rowFocusMode)this._itemsFocusable=t;else{let i=this._itemsFocusable.parentElement,n=this._itemsFocusable;if(i){this.__isCell(i)&&(n=i,i=i.parentElement);const s=[...i.children].indexOf(n);this._itemsFocusable=this.__getFocusable(t,t.children[s])}}}),e&&this._itemsFocusable.focus()}_onKeyDown(e){const t=e.key;let i;switch(t){case"ArrowUp":case"ArrowDown":case"ArrowLeft":case"ArrowRight":case"PageUp":case"PageDown":case"Home":case"End":i="Navigation";break;case"Enter":case"Escape":case"F2":i="Interaction";break;case"Tab":i="Tab";break;case" ":i="Space";break}this._detectInteracting(e),this.interacting&&i!=="Interaction"&&(i=void 0),i&&this[`_on${i}KeyDown`](e,t)}_ensureScrolledToIndex(e){[...this.$.items.children].find(i=>i.index===e)?this.__scrollIntoViewport(e):this.scrollToIndex(e)}__isRowExpandable(e){if(this.itemHasChildrenPath){const t=e._item;return!!(t&&ge(this.itemHasChildrenPath,t)&&!this._isExpanded(t))}}__isRowCollapsible(e){return this._isExpanded(e._item)}__isDetailsCell(e){return e.matches('[part~="details-cell"]')}__isCell(e){return e instanceof HTMLTableCellElement}__isRow(e){return e instanceof HTMLTableRowElement}__getIndexOfChildElement(e){return Array.prototype.indexOf.call(e.parentNode.children,e)}_onNavigationKeyDown(e,t){e.preventDefault();const i=this.__isRTL,n=e.composedPath().find(m=>this.__isRow(m)),s=e.composedPath().find(m=>this.__isCell(m));let l=0,h=0;switch(t){case"ArrowRight":l=i?-1:1;break;case"ArrowLeft":l=i?1:-1;break;case"Home":this.__rowFocusMode||e.ctrlKey?h=-1/0:l=-1/0;break;case"End":this.__rowFocusMode||e.ctrlKey?h=1/0:l=1/0;break;case"ArrowDown":h=1;break;case"ArrowUp":h=-1;break;case"PageDown":if(this.$.items.contains(n)){const m=this.__getIndexInGroup(n,this._focusedItemIndex);this._scrollToFlatIndex(m)}h=this._visibleItemsCount;break;case"PageUp":h=-this._visibleItemsCount;break}if(this.__rowFocusMode&&!n||!this.__rowFocusMode&&!s)return;const p=i?"ArrowLeft":"ArrowRight",_=i?"ArrowRight":"ArrowLeft";if(t===p){if(this.__rowFocusMode){if(this.__isRowExpandable(n)){this.expandItem(n._item);return}this.__rowFocusMode=!1,this._onCellNavigation(n.firstElementChild,0,0);return}}else if(t===_)if(this.__rowFocusMode){if(this.__isRowCollapsible(n)){this.collapseItem(n._item);return}}else{const m=[...n.children].sort((g,b)=>g._order-b._order);if(s===m[0]||this.__isDetailsCell(s)){this.__rowFocusMode=!0,this._onRowNavigation(n,0);return}}this.__rowFocusMode?this._onRowNavigation(n,h):this._onCellNavigation(s,l,h)}_onRowNavigation(e,t){const{dstRow:i}=this.__navigateRows(t,e);i&&i.focus()}__getIndexInGroup(e,t){return e.parentNode===this.$.items?t!==void 0?t:e.index:this.__getIndexOfChildElement(e)}__navigateRows(e,t,i){const n=this.__getIndexInGroup(t,this._focusedItemIndex),s=t.parentNode,l=(s===this.$.items?this._flatSize:s.children.length)-1;let h=Math.max(0,Math.min(n+e,l));if(s!==this.$.items){if(h>n)for(;h<l&&s.children[h].hidden;)h+=1;else if(h<n)for(;h>0&&s.children[h].hidden;)h-=1;return this.toggleAttribute("navigating",!0),{dstRow:s.children[h]}}let p=!1;if(i){const _=this.__isDetailsCell(i);if(s===this.$.items){const m=t._item,{item:g}=this._dataProviderController.getFlatIndexContext(h);_?p=e===0:p=e===1&&this._isDetailsOpened(m)||e===-1&&h!==n&&this._isDetailsOpened(g),p!==_&&(e===1&&p||e===-1&&!p)&&(h=n)}}return this._ensureScrolledToIndex(h),this._focusedItemIndex=h,this.toggleAttribute("navigating",!0),{dstRow:[...s.children].find(_=>!_.hidden&&_.index===h),dstIsRowDetails:p}}_onCellNavigation(e,t,i){const n=e.parentNode,{dstRow:s,dstIsRowDetails:l}=this.__navigateRows(i,n,e);if(!s)return;let h=this.__getIndexOfChildElement(e);this.$.items.contains(e)&&(h=[...this.$.sizer.children].findIndex(g=>g._column===e._column));const p=this.__isDetailsCell(e),_=n.parentNode,m=this.__getIndexInGroup(n,this._focusedItemIndex);if(this._focusedColumnOrder===void 0&&(p?this._focusedColumnOrder=0:this._focusedColumnOrder=this._getColumns(_,m).filter(g=>!g.hidden)[h]._order),l)[...s.children].find(b=>this.__isDetailsCell(b)).focus();else{const g=this.__getIndexInGroup(s,this._focusedItemIndex),b=this._getColumns(_,g).filter(E=>!E.hidden),v=b.map(E=>E._order).sort((E,$)=>E-$),S=v.length-1,D=v.indexOf(v.slice(0).sort((E,$)=>Math.abs(E-this._focusedColumnOrder)-Math.abs($-this._focusedColumnOrder))[0]),L=i===0&&p?D:Math.max(0,Math.min(D+t,S));L!==D&&(this._focusedColumnOrder=void 0);const P=b.reduce((E,$,K)=>(E[$._order]=K,E),{})[v[L]];let O;if(this.$.items.contains(e)){const E=this.$.sizer.children[P];this._lazyColumns&&(this.__isColumnInViewport(E._column)||E.scrollIntoView(),this.__updateColumnsBodyContentHidden(),this.__updateHorizontalScrollPosition()),O=[...s.children].find($=>$._column===E._column),this._scrollHorizontallyToCell(O)}else O=s.children[P],this._scrollHorizontallyToCell(O);O.focus()}}_onInteractionKeyDown(e,t){const i=e.composedPath()[0],n=i.localName==="input"&&!/^(button|checkbox|color|file|image|radio|range|reset|submit)$/iu.test(i.type);let s;switch(t){case"Enter":s=this.interacting?!n:!0;break;case"Escape":s=!1;break;case"F2":s=!this.interacting;break}const{cell:l}=this._getGridEventLocation(e);if(this.interacting!==s&&l!==null)if(s){const h=l._content.querySelector("[focus-target]")||[...l._content.querySelectorAll("*")].find(p=>this._isFocusable(p));h&&(e.preventDefault(),h.focus(),this._setInteracting(!0),this.toggleAttribute("navigating",!1))}else e.preventDefault(),this._focusedColumnOrder=void 0,l.focus(),this._setInteracting(!1),this.toggleAttribute("navigating",!0);t==="Escape"&&this._hideTooltip(!0)}_predictFocusStepTarget(e,t){const i=[this.$.table,this._headerFocusable,this._itemsFocusable,this._footerFocusable,this.$.focusexit];let n=i.indexOf(e);for(n+=t;n>=0&&n<=i.length-1;){let l=i[n];if(l&&!this.__rowFocusMode&&(l=i[n].parentNode),!l||l.hidden)n+=t;else break}let s=i[n];if(s&&!this.__isHorizontallyInViewport(s)){const l=this._getColumnsInOrder().find(h=>this.__isColumnInViewport(h));if(l)if(s===this._headerFocusable)s=l._headerCell;else if(s===this._itemsFocusable){const h=s._column._cells.indexOf(s);s=l._cells[h]}else s===this._footerFocusable&&(s=l._footerCell)}return s}_onTabKeyDown(e){const t=this._predictFocusStepTarget(e.composedPath()[0],e.shiftKey?-1:1);if(t){if(e.stopPropagation(),t===this.$.table)this.$.table.focus();else if(t===this.$.focusexit)this.$.focusexit.focus();else if(t===this._itemsFocusable){let i=t;const n=this.__isRow(t)?t:t.parentNode;if(this._ensureScrolledToIndex(this._focusedItemIndex),n.index!==this._focusedItemIndex&&this.__isCell(t)){const s=Array.from(n.children).indexOf(this._itemsFocusable),l=Array.from(this.$.items.children).find(h=>!h.hidden&&h.index===this._focusedItemIndex);l&&(i=l.children[s])}e.preventDefault(),i.focus()}else e.preventDefault(),t.focus();this.toggleAttribute("navigating",!0)}}_onSpaceKeyDown(e){e.preventDefault();const t=e.composedPath()[0],i=this.__isRow(t);(i||!t._content||!t._content.firstElementChild)&&this.dispatchEvent(new CustomEvent(i?"row-activate":"cell-activate",{detail:{model:this.__getRowModel(i?t:t.parentElement)}}))}_onKeyUp(e){if(!/^( |SpaceBar)$/u.test(e.key)||this.interacting)return;e.preventDefault();const t=e.composedPath()[0];if(t._content&&t._content.firstElementChild){const i=this.hasAttribute("navigating");t._content.firstElementChild.dispatchEvent(new MouseEvent("click",{shiftKey:e.shiftKey,bubbles:!0,composed:!0,cancelable:!0})),this.toggleAttribute("navigating",i)}}_onFocusIn(e){this._isMousedown||this.toggleAttribute("navigating",!0);const t=e.composedPath()[0];t===this.$.table||t===this.$.focusexit?(this._isMousedown||this._predictFocusStepTarget(t,t===this.$.table?1:-1).focus(),this._setInteracting(!1)):this._detectInteracting(e)}_onFocusOut(e){this.toggleAttribute("navigating",!1),this._detectInteracting(e),this._hideTooltip(),this._focusedCell=null}_onContentFocusIn(e){const{section:t,cell:i,row:n}=this._getGridEventLocation(e);if(!(!i&&!this.__rowFocusMode)){if(this._detectInteracting(e),t&&(i||n))if(this._activeRowGroup=t,this.$.header===t?this._headerFocusable=this.__getFocusable(n,i):this.$.items===t?this._itemsFocusable=this.__getFocusable(n,i):this.$.footer===t&&(this._footerFocusable=this.__getFocusable(n,i)),i){const s=this.getEventContext(e);this.__pendingBodyCellFocus=this.loading&&s.section==="body",this.__pendingBodyCellFocus||i.dispatchEvent(new CustomEvent("cell-focus",{bubbles:!0,composed:!0,detail:{context:s}})),this._focusedCell=i._focusButton||i,Ye()&&e.target===i&&this._showTooltip(e)}else this._focusedCell=null;this._detectFocusedItemIndex(e)}}__dispatchPendingBodyCellFocus(){this.__pendingBodyCellFocus&&this.shadowRoot.activeElement===this._itemsFocusable&&this._itemsFocusable.dispatchEvent(new Event("focusin",{bubbles:!0,composed:!0}))}__getFocusable(e,t){return this.__rowFocusMode?e:t._focusButton||t}_detectInteracting(e){const t=e.composedPath().some(i=>i.localName==="vaadin-grid-cell-content");this._setInteracting(t),this.__updateHorizontalScrollPosition()}_detectFocusedItemIndex(e){const{section:t,row:i}=this._getGridEventLocation(e);t===this.$.items&&(this._focusedItemIndex=i.index)}_updateGridSectionFocusTarget(e){if(!e)return;const t=this._getGridSectionFromFocusTarget(e),i=this.interacting&&t===this._activeRowGroup;e.tabIndex=i?-1:0}_preventScrollerRotatingCellFocus(e,t){e.index===this._focusedItemIndex&&this.hasAttribute("navigating")&&this._activeRowGroup===this.$.items&&(this._navigatingIsHidden=!0,this.toggleAttribute("navigating",!1)),t===this._focusedItemIndex&&this._navigatingIsHidden&&(this._navigatingIsHidden=!1,this.toggleAttribute("navigating",!0))}_getColumns(e,t){let i=this._columnTree.length-1;return e===this.$.header?i=t:e===this.$.footer&&(i=this._columnTree.length-1-t),this._columnTree[i]}__isValidFocusable(e){return this.$.table.contains(e)&&e.offsetHeight}_resetKeyboardNavigation(){if(!this.$&&this.performUpdate&&this.performUpdate(),["header","footer"].forEach(e=>{if(!this.__isValidFocusable(this[`_${e}Focusable`])){const t=[...this.$[e].children].find(n=>n.offsetHeight),i=t?[...t.children].find(n=>!n.hidden):null;t&&i&&(this[`_${e}Focusable`]=this.__getFocusable(t,i))}}),!this.__isValidFocusable(this._itemsFocusable)&&this.$.items.firstElementChild){const e=this.__getFirstVisibleItem(),t=e?[...e.children].find(i=>!i.hidden):null;t&&e&&(this._focusedColumnOrder=void 0,this._itemsFocusable=this.__getFocusable(e,t))}else this.__updateItemsFocusable()}_scrollHorizontallyToCell(e){if(e.hasAttribute("frozen")||e.hasAttribute("frozen-to-end")||this.__isDetailsCell(e))return;const t=e.getBoundingClientRect(),i=e.parentNode,n=Array.from(i.children).indexOf(e),s=this.$.table.getBoundingClientRect();let l=s.left,h=s.right;for(let p=n-1;p>=0;p--){const _=i.children[p];if(!(_.hasAttribute("hidden")||this.__isDetailsCell(_))&&(_.hasAttribute("frozen")||_.hasAttribute("frozen-to-end"))){l=_.getBoundingClientRect().right;break}}for(let p=n+1;p<i.children.length;p++){const _=i.children[p];if(!(_.hasAttribute("hidden")||this.__isDetailsCell(_))&&(_.hasAttribute("frozen")||_.hasAttribute("frozen-to-end"))){h=_.getBoundingClientRect().left;break}}t.left<l&&(this.$.table.scrollLeft+=Math.round(t.left-l)),t.right>h&&(this.$.table.scrollLeft+=Math.round(t.right-h))}_getGridEventLocation(e){const t=e.composedPath(),i=t.indexOf(this.$.table),n=i>=1?t[i-1]:null,s=i>=2?t[i-2]:null,l=i>=3?t[i-3]:null;return{section:n,row:s,cell:l}}_getGridSectionFromFocusTarget(e){return e===this._headerFocusable?this.$.header:e===this._itemsFocusable?this.$.items:e===this._footerFocusable?this.$.footer:null}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Tt=o=>class extends o{static get properties(){return{detailsOpenedItems:{type:Array,value:()=>[],sync:!0},rowDetailsRenderer:{type:Function,sync:!0},_detailsCells:{type:Array}}}static get observers(){return["_detailsOpenedItemsChanged(detailsOpenedItems, rowDetailsRenderer)","_rowDetailsRendererChanged(rowDetailsRenderer)"]}ready(){super.ready(),this._detailsCellResizeObserver=new ResizeObserver(e=>{e.forEach(({target:t})=>{this._updateDetailsCellHeight(t.parentElement)}),this.__virtualizer.__adapter._resizeHandler()})}_rowDetailsRendererChanged(e){e&&this._columnTree&&z(this.$.items,t=>{if(!t.querySelector("[part~=details-cell]")){this._updateRow(t,this._columnTree[this._columnTree.length-1]);const i=this._isDetailsOpened(t._item);this._toggleDetailsCell(t,i)}})}_detailsOpenedItemsChanged(e,t){z(this.$.items,i=>{if(i.hasAttribute("details-opened")){this._updateItem(i,i._item);return}t&&this._isDetailsOpened(i._item)&&this._updateItem(i,i._item)})}_configureDetailsCell(e){e.setAttribute("part","cell details-cell"),e.toggleAttribute("frozen",!0),this._detailsCellResizeObserver.observe(e)}_toggleDetailsCell(e,t){const i=e.querySelector('[part~="details-cell"]');i&&(i.hidden=!t,!i.hidden&&this.rowDetailsRenderer&&(i._renderer=this.rowDetailsRenderer))}_updateDetailsCellHeight(e){const t=e.querySelector('[part~="details-cell"]');t&&(this.__updateDetailsRowPadding(e,t),requestAnimationFrame(()=>this.__updateDetailsRowPadding(e,t)))}__updateDetailsRowPadding(e,t){t.hidden?e.style.removeProperty("padding-bottom"):e.style.setProperty("padding-bottom",`${t.offsetHeight}px`)}_updateDetailsCellHeights(){z(this.$.items,e=>{this._updateDetailsCellHeight(e)})}_isDetailsOpened(e){return this.detailsOpenedItems&&this._getItemIndexInArray(e,this.detailsOpenedItems)!==-1}openItemDetails(e){this._isDetailsOpened(e)||(this.detailsOpenedItems=[...this.detailsOpenedItems,e])}closeItemDetails(e){this._isDetailsOpened(e)&&(this.detailsOpenedItems=this.detailsOpenedItems.filter(t=>!this._itemsEqual(t,e)))}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const ze={SCROLLING:500,UPDATE_CONTENT_VISIBILITY:100},Dt=o=>class extends Qe(o){static get properties(){return{columnRendering:{type:String,value:"eager",sync:!0},_frozenCells:{type:Array,value:()=>[]},_frozenToEndCells:{type:Array,value:()=>[]},_rowWithFocusedElement:Element}}static get observers(){return["__columnRenderingChanged(_columnTree, columnRendering)"]}get _scrollLeft(){return this.$.table.scrollLeft}get _scrollTop(){return this.$.table.scrollTop}set _scrollTop(e){this.$.table.scrollTop=e}get _lazyColumns(){return this.columnRendering==="lazy"}ready(){super.ready(),this.scrollTarget=this.$.table,this.$.items.addEventListener("focusin",e=>{const t=e.composedPath().indexOf(this.$.items);this._rowWithFocusedElement=e.composedPath()[t-1],this._rowWithFocusedElement&&(this._isMousedown||this.__scrollIntoViewport(this._rowWithFocusedElement.index),this.$.table.contains(e.relatedTarget)||this.$.table.dispatchEvent(new CustomEvent("virtualizer-element-focused",{detail:{element:this._rowWithFocusedElement}})))}),this.$.items.addEventListener("focusout",()=>{this._rowWithFocusedElement=void 0}),this.$.table.addEventListener("scroll",()=>this._afterScroll())}_onResize(){if(this._updateOverflow(),this.__updateHorizontalScrollPosition(),this._firefox){const e=!te(this);e&&this.__previousVisible===!1&&(this._scrollTop=this.__memorizedScrollTop||0),this.__previousVisible=e}}_scrollToFlatIndex(e){e=Math.min(this._flatSize-1,Math.max(0,e)),this.__virtualizer.scrollToIndex(e),this.__scrollIntoViewport(e)}__scrollIntoViewport(e){const t=[...this.$.items.children].find(i=>i.index===e);if(t){const i=t.getBoundingClientRect(),n=this.$.footer.getBoundingClientRect().top,s=this.$.header.getBoundingClientRect().bottom;i.bottom>n?this.$.table.scrollTop+=i.bottom-n:i.top<s&&(this.$.table.scrollTop-=s-i.top)}}_scheduleScrolling(){this._scrollingFrame||(this._scrollingFrame=requestAnimationFrame(()=>this.$.scroller.toggleAttribute("scrolling",!0))),this._debounceScrolling=R.debounce(this._debounceScrolling,X.after(ze.SCROLLING),()=>{cancelAnimationFrame(this._scrollingFrame),delete this._scrollingFrame,this.$.scroller.toggleAttribute("scrolling",!1)})}_afterScroll(){this.__updateHorizontalScrollPosition(),this.hasAttribute("reordering")||this._scheduleScrolling(),this.hasAttribute("navigating")||this._hideTooltip(!0),this._updateOverflow(),this._debounceColumnContentVisibility=R.debounce(this._debounceColumnContentVisibility,X.after(ze.UPDATE_CONTENT_VISIBILITY),()=>{this._lazyColumns&&this.__cachedScrollLeft!==this._scrollLeft&&(this.__cachedScrollLeft=this._scrollLeft,this.__updateColumnsBodyContentHidden())}),this._firefox&&!te(this)&&this.__previousVisible!==!1&&(this.__memorizedScrollTop=this._scrollTop)}__updateColumnsBodyContentHidden(){if(!this._columnTree||!this._areSizerCellsAssigned())return;const e=this._getColumnsInOrder();let t=!1;if(e.forEach(i=>{const n=this._lazyColumns&&!this.__isColumnInViewport(i);i._bodyContentHidden!==n&&(t=!0,i._cells.forEach(s=>{if(s!==i._sizerCell){if(n)s.remove();else if(s.__parentRow){const l=[...s.__parentRow.children].find(h=>e.indexOf(h._column)>e.indexOf(i));s.__parentRow.insertBefore(s,l)}}})),i._bodyContentHidden=n}),t&&this._frozenCellsChanged(),this._lazyColumns){const i=[...e].reverse().find(l=>l.frozen),n=this.__getColumnEnd(i),s=e.find(l=>!l.frozen&&!l._bodyContentHidden);this.__lazyColumnsStart=this.__getColumnStart(s)-n,this.$.items.style.setProperty("--_grid-lazy-columns-start",`${this.__lazyColumnsStart}px`),this._resetKeyboardNavigation()}}__getColumnEnd(e){return e?e._sizerCell.offsetLeft+(this.__isRTL?0:e._sizerCell.offsetWidth):this.__isRTL?this.$.table.clientWidth:0}__getColumnStart(e){return e?e._sizerCell.offsetLeft+(this.__isRTL?e._sizerCell.offsetWidth:0):this.__isRTL?this.$.table.clientWidth:0}__isColumnInViewport(e){return e.frozen||e.frozenToEnd?!0:this.__isHorizontallyInViewport(e._sizerCell)}__isHorizontallyInViewport(e){return e.offsetLeft+e.offsetWidth>=this._scrollLeft&&e.offsetLeft<=this._scrollLeft+this.clientWidth}__columnRenderingChanged(e,t){t==="eager"?this.$.scroller.removeAttribute("column-rendering"):this.$.scroller.setAttribute("column-rendering",t),this.__updateColumnsBodyContentHidden()}_updateOverflow(){this._debounceOverflow=R.debounce(this._debounceOverflow,se,()=>{this.__doUpdateOverflow()})}__doUpdateOverflow(){let e="";const t=this.$.table;t.scrollTop<t.scrollHeight-t.clientHeight&&(e+=" bottom"),t.scrollTop>0&&(e+=" top");const i=Ae(t,this.getAttribute("dir"));i>0&&(e+=" start"),i<t.scrollWidth-t.clientWidth&&(e+=" end"),this.__isRTL&&(e=e.replace(/start|end/giu,s=>s==="start"?"end":"start")),t.scrollLeft<t.scrollWidth-t.clientWidth&&(e+=" right"),t.scrollLeft>0&&(e+=" left");const n=e.trim();n.length>0&&this.getAttribute("overflow")!==n?this.setAttribute("overflow",n):n.length===0&&this.hasAttribute("overflow")&&this.removeAttribute("overflow")}_frozenCellsChanged(){this._debouncerCacheElements=R.debounce(this._debouncerCacheElements,V,()=>{Array.from(this.shadowRoot.querySelectorAll('[part~="cell"]')).forEach(e=>{e.style.transform=""}),this._frozenCells=Array.prototype.slice.call(this.$.table.querySelectorAll("[frozen]")),this._frozenToEndCells=Array.prototype.slice.call(this.$.table.querySelectorAll("[frozen-to-end]")),this.__updateHorizontalScrollPosition()}),this._debounceUpdateFrozenColumn()}_debounceUpdateFrozenColumn(){this.__debounceUpdateFrozenColumn=R.debounce(this.__debounceUpdateFrozenColumn,V,()=>this._updateFrozenColumn())}_updateFrozenColumn(){if(!this._columnTree)return;const e=this._columnTree[this._columnTree.length-1].slice(0);e.sort((n,s)=>n._order-s._order);let t,i;for(let n=0;n<e.length;n++){const s=e[n];s._lastFrozen=!1,s._firstFrozenToEnd=!1,i===void 0&&s.frozenToEnd&&!s.hidden&&(i=n),s.frozen&&!s.hidden&&(t=n)}t!==void 0&&(e[t]._lastFrozen=!0),i!==void 0&&(e[i]._firstFrozenToEnd=!0),this.__updateColumnsBodyContentHidden()}__updateHorizontalScrollPosition(){if(!this._columnTree)return;const e=this.$.table.scrollWidth,t=this.$.table.clientWidth,i=Math.max(0,this.$.table.scrollLeft),n=Ae(this.$.table,this.getAttribute("dir")),s=`translate(${-i}px, 0)`;this.$.header.style.transform=s,this.$.footer.style.transform=s,this.$.items.style.transform=s;const l=this.__isRTL?n+t-e:i,h=`translate(${l}px, 0)`;this._frozenCells.forEach(g=>{g.style.transform=h});const p=this.__isRTL?n:i+t-e,_=`translate(${p}px, 0)`;let m=_;if(this._lazyColumns&&this._areSizerCellsAssigned()){const g=this._getColumnsInOrder(),b=[...g].reverse().find(I=>!I.frozenToEnd&&!I._bodyContentHidden),v=this.__getColumnEnd(b),S=g.find(I=>I.frozenToEnd),D=this.__getColumnStart(S);m=`translate(${p+(D-v)+this.__lazyColumnsStart}px, 0)`}this._frozenToEndCells.forEach(g=>{this.$.items.contains(g)?g.style.transform=m:g.style.transform=_}),this.hasAttribute("navigating")&&this.__rowFocusMode&&this.$.table.style.setProperty("--_grid-horizontal-scroll-position",`${-l}px`)}_areSizerCellsAssigned(){return this._getColumnsInOrder().every(e=>e._sizerCell)}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const $t=o=>class extends o{static get properties(){return{selectedItems:{type:Object,notify:!0,value:()=>[],sync:!0},__selectedKeys:{type:Object,computed:"__computeSelectedKeys(itemIdPath, selectedItems)"}}}static get observers(){return["__selectedItemsChanged(itemIdPath, selectedItems)"]}_isSelected(e){return this.__selectedKeys.has(this.getItemId(e))}selectItem(e){this._isSelected(e)||(this.selectedItems=[...this.selectedItems,e])}deselectItem(e){this._isSelected(e)&&(this.selectedItems=this.selectedItems.filter(t=>!this._itemsEqual(t,e)))}_toggleItem(e){this._isSelected(e)?this.deselectItem(e):this.selectItem(e)}__selectedItemsChanged(){this.requestContentUpdate()}__computeSelectedKeys(e,t){const i=t||[],n=new Set;return i.forEach(s=>{n.add(this.getItemId(s))}),n}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */let Fe="prepend";const Ot=o=>class extends o{static get properties(){return{multiSort:{type:Boolean,value:!1},multiSortPriority:{type:String,value:()=>Fe},multiSortOnShiftClick:{type:Boolean,value:!1},_sorters:{type:Array,value:()=>[]},_previousSorters:{type:Array,value:()=>[]}}}static setDefaultMultiSortPriority(e){Fe=["append","prepend"].includes(e)?e:"prepend"}ready(){super.ready(),this.addEventListener("sorter-changed",this._onSorterChanged)}_onSorterChanged(e){const t=e.target;e.stopPropagation(),t._grid=this,this.__updateSorter(t,e.detail.shiftClick,e.detail.fromSorterClick),this.__applySorters()}__removeSorters(e){e.length!==0&&(this._sorters=this._sorters.filter(t=>!e.includes(t)),this.__applySorters())}__updateSortOrders(){this._sorters.forEach(t=>{t._order=null});const e=this._getActiveSorters();e.length>1&&e.forEach((t,i)=>{t._order=i})}__updateSorter(e,t,i){if(!e.direction&&!this._sorters.includes(e))return;e._order=null;const n=this._sorters.filter(s=>s!==e);this.multiSort&&(!this.multiSortOnShiftClick||!i)||this.multiSortOnShiftClick&&t?this.multiSortPriority==="append"?this._sorters=[...n,e]:this._sorters=[e,...n]:(e.direction||this.multiSortOnShiftClick)&&(this._sorters=e.direction?[e]:[],n.forEach(s=>{s._order=null,s.direction=null}))}__applySorters(){this.__updateSortOrders(),this.dataProvider&&this.isAttached&&JSON.stringify(this._previousSorters)!==JSON.stringify(this._mapSorters())&&this.__debounceClearCache(),this._a11yUpdateSorters(),this._previousSorters=this._mapSorters()}_getActiveSorters(){return this._sorters.filter(e=>e.direction&&e.isConnected)}_mapSorters(){return this._getActiveSorters().map(e=>({path:e.path,direction:e.direction}))}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Lt=o=>class extends o{static get properties(){return{cellClassNameGenerator:{type:Function,sync:!0},cellPartNameGenerator:{type:Function,sync:!0}}}static get observers(){return["__cellClassNameGeneratorChanged(cellClassNameGenerator)","__cellPartNameGeneratorChanged(cellPartNameGenerator)"]}__cellClassNameGeneratorChanged(){this.generateCellClassNames()}__cellPartNameGeneratorChanged(){this.generateCellPartNames()}generateCellClassNames(){z(this.$.items,e=>{e.hidden||this._generateCellClassNames(e,this.__getRowModel(e))})}generateCellPartNames(){z(this.$.items,e=>{e.hidden||this._generateCellPartNames(e,this.__getRowModel(e))})}_generateCellClassNames(e,t){q(e,i=>{if(i.__generatedClasses&&i.__generatedClasses.forEach(n=>i.classList.remove(n)),this.cellClassNameGenerator&&!e.hasAttribute("loading")){const n=this.cellClassNameGenerator(i._column,t);i.__generatedClasses=n&&n.split(" ").filter(s=>s.length>0),i.__generatedClasses&&i.__generatedClasses.forEach(s=>i.classList.add(s))}})}_generateCellPartNames(e,t){q(e,i=>{if(i.__generatedParts&&i.__generatedParts.forEach(n=>{N(i,null,n)}),this.cellPartNameGenerator&&!e.hasAttribute("loading")){const n=this.cellPartNameGenerator(i._column,t);i.__generatedParts=n&&n.split(" ").filter(s=>s.length>0),i.__generatedParts&&i.__generatedParts.forEach(s=>{N(i,!0,s)})}})}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const kt=o=>class extends yt(It(Rt(mt(Dt($t(Ot(Tt(Pt(gt(Ft(At(wt(zt(St(Lt(Ze(o))))))))))))))))){static get observers(){return["_columnTreeChanged(_columnTree)","_flatSizeChanged(_flatSize, __virtualizer, _hasData, _columnTree)"]}static get properties(){return{_safari:{type:Boolean,value:Xe},_ios:{type:Boolean,value:we},_firefox:{type:Boolean,value:Je},_android:{type:Boolean,value:xe},_touchDevice:{type:Boolean,value:Oe},allRowsVisible:{type:Boolean,value:!1,reflectToAttribute:!0},__pendingRecalculateColumnWidths:{type:Boolean,value:!0},isAttached:{value:!1},__gridElement:{type:Boolean,value:!0}}}constructor(){super(),this.addEventListener("animationend",this._onAnimationEnd)}get _firstVisibleIndex(){const r=this.__getFirstVisibleItem();return r?r.index:void 0}get _lastVisibleIndex(){const r=this.__getLastVisibleItem();return r?r.index:void 0}connectedCallback(){super.connectedCallback(),this.isAttached=!0,this.recalculateColumnWidths()}disconnectedCallback(){super.disconnectedCallback(),this.isAttached=!1,this._hideTooltip(!0)}__getFirstVisibleItem(){return this._getRenderedRows().find(r=>this._isInViewport(r))}__getLastVisibleItem(){return this._getRenderedRows().reverse().find(r=>this._isInViewport(r))}_isInViewport(r){const e=this.$.table.getBoundingClientRect(),t=r.getBoundingClientRect(),i=this.$.header.getBoundingClientRect().height,n=this.$.footer.getBoundingClientRect().height;return t.bottom>e.top+i&&t.top<e.bottom-n}_getRenderedRows(){return Array.from(this.$.items.children).filter(r=>!r.hidden).sort((r,e)=>r.index-e.index)}_getRowContainingNode(r){const e=et("vaadin-grid-cell-content",r);return e?e.assignedSlot.parentElement.parentElement:void 0}_isItemAssignedToRow(r,e){const t=this.__getRowModel(e);return this.getItemId(r)===this.getItemId(t.item)}ready(){super.ready(),this.__virtualizer=new lt({createElements:this._createScrollerRows.bind(this),updateElement:this._updateScrollerItem.bind(this),scrollContainer:this.$.items,scrollTarget:this.$.table,reorderElements:!0}),new ResizeObserver(()=>setTimeout(()=>{this.__updateColumnsBodyContentHidden(),this.__tryToRecalculateColumnWidthsIfPending()})).observe(this.$.table),De(this),this._tooltipController=new tt(this),this.addController(this._tooltipController),this._tooltipController.setManual(!0)}__getBodyCellCoordinates(r){if(this.$.items.contains(r)&&r.localName==="td")return{item:r.parentElement._item,column:r._column}}__focusBodyCell({item:r,column:e}){const t=this._getRenderedRows().find(n=>n._item===r),i=t&&[...t.children].find(n=>n._column===e);i&&i.focus()}_focusFirstVisibleRow(){const r=this.__getFirstVisibleItem();this.__rowFocusMode=!0,r.focus()}_flatSizeChanged(r,e,t,i){if(e&&t&&i){const n=this.shadowRoot.activeElement,s=this.__getBodyCellCoordinates(n),l=e.size||0;e.size=r,e.update(l-1,l-1),r<l&&e.update(r-1,r-1),s&&n.parentElement.hidden&&this.__focusBodyCell(s),this._resetKeyboardNavigation()}}__getIntrinsicWidth(r){return this.__intrinsicWidthCache.has(r)||this.__calculateAndCacheIntrinsicWidths([r]),this.__intrinsicWidthCache.get(r)}__getDistributedWidth(r,e){if(r==null||r===this)return 0;const t=Math.max(this.__getIntrinsicWidth(r),this.__getDistributedWidth((r.assignedSlot||r).parentElement,r));if(!e)return t;const i=r,n=t,s=i._visibleChildColumns.map(_=>this.__getIntrinsicWidth(_)).reduce((_,m)=>_+m,0),l=Math.max(0,n-s),p=this.__getIntrinsicWidth(e)/s*l;return this.__getIntrinsicWidth(e)+p}_recalculateColumnWidths(r){this.__virtualizer.flush(),[...this.$.header.children,...this.$.footer.children].forEach(i=>{i.__debounceUpdateHeaderFooterRowVisibility&&i.__debounceUpdateHeaderFooterRowVisibility.flush()}),this._debouncerHiddenChanged&&this._debouncerHiddenChanged.flush(),this.__intrinsicWidthCache=new Map;const e=this._firstVisibleIndex,t=this._lastVisibleIndex;this.__viewportRowsCache=this._getRenderedRows().filter(i=>i.index>=e&&i.index<=t),this.__calculateAndCacheIntrinsicWidths(r),r.forEach(i=>{i.width=`${this.__getDistributedWidth(i)}px`})}__setVisibleCellContentAutoWidth(r,e){r._allCells.filter(t=>this.$.items.contains(t)?this.__viewportRowsCache.includes(t.parentElement):!0).forEach(t=>{t.__measuringAutoWidth=e,t.__measuringAutoWidth?(t.__originalWidth=t.style.width,t.style.width="auto",t.style.position="absolute"):(t.style.width=t.__originalWidth,delete t.__originalWidth,t.style.position="")}),e?this.$.scroller.setAttribute("measuring-auto-width",""):this.$.scroller.removeAttribute("measuring-auto-width")}__getAutoWidthCellsMaxWidth(r){return r._allCells.reduce((e,t)=>t.__measuringAutoWidth?Math.max(e,t.offsetWidth+1):e,0)}__calculateAndCacheIntrinsicWidths(r){r.forEach(e=>this.__setVisibleCellContentAutoWidth(e,!0)),r.forEach(e=>{const t=this.__getAutoWidthCellsMaxWidth(e);this.__intrinsicWidthCache.set(e,t)}),r.forEach(e=>this.__setVisibleCellContentAutoWidth(e,!1))}recalculateColumnWidths(){if(!this._columnTree)return;if(te(this)||this._dataProviderController.isLoading()){this.__pendingRecalculateColumnWidths=!0;return}const r=this._getColumns().filter(t=>!t.hidden&&t.autoWidth),e=r.filter(t=>!customElements.get(t.localName));e.length?Promise.all(e.map(t=>customElements.whenDefined(t.localName))).then(()=>{this._recalculateColumnWidths(r)}):this._recalculateColumnWidths(r)}__tryToRecalculateColumnWidthsIfPending(){if(!this.__pendingRecalculateColumnWidths||te(this)||this._dataProviderController.isLoading()||[...this.$.items.children].some(t=>t.index===void 0))return;[...this.$.items.children].some(t=>t.clientHeight>0)&&(this.__pendingRecalculateColumnWidths=!1,this.recalculateColumnWidths())}_onDataProviderPageLoaded(){super._onDataProviderPageLoaded(),this.__tryToRecalculateColumnWidthsIfPending()}_createScrollerRows(r){const e=[];for(let t=0;t<r;t++){const i=document.createElement("tr");i.setAttribute("part","row body-row"),i.setAttribute("role","row"),i.setAttribute("tabindex","-1"),this._columnTree&&this._updateRow(i,this._columnTree[this._columnTree.length-1],"body",!1,!0),e.push(i)}return this._columnTree&&this._columnTree[this._columnTree.length-1].forEach(t=>{t.isConnected&&t._cells&&(t._cells=[...t._cells])}),this.__afterCreateScrollerRowsDebouncer=R.debounce(this.__afterCreateScrollerRowsDebouncer,se,()=>{this._afterScroll(),this.__tryToRecalculateColumnWidthsIfPending()}),e}_createCell(r,e){const i=`vaadin-grid-cell-content-${this._contentIndex=this._contentIndex+1||0}`,n=document.createElement("vaadin-grid-cell-content");n.setAttribute("slot",i);const s=document.createElement(r);s.id=i.replace("-content-","-"),s.setAttribute("role",r==="td"?"gridcell":"columnheader"),!xe&&!we&&(s.addEventListener("mouseenter",h=>{this.$.scroller.hasAttribute("scrolling")||this._showTooltip(h)}),s.addEventListener("mouseleave",()=>{this._hideTooltip()}),s.addEventListener("mousedown",()=>{this._hideTooltip(!0)}));const l=document.createElement("slot");if(l.setAttribute("name",i),e&&e._focusButtonMode){const h=document.createElement("div");h.setAttribute("role","button"),h.setAttribute("tabindex","-1"),s.appendChild(h),s._focusButton=h,s.focus=function(p){s._focusButton.focus(p)},h.appendChild(l)}else s.setAttribute("tabindex","-1"),s.appendChild(l);return s._content=n,n.addEventListener("mousedown",()=>{if(it){const h=p=>{const _=n.contains(this.getRootNode().activeElement),m=p.composedPath().includes(n);!_&&m&&s.focus({preventScroll:!0}),document.removeEventListener("mouseup",h,!0)};document.addEventListener("mouseup",h,!0)}else setTimeout(()=>{n.contains(this.getRootNode().activeElement)||s.focus({preventScroll:!0})})}),s}_updateRow(r,e,t="body",i=!1,n=!1){const s=document.createDocumentFragment();q(r,l=>{l._vacant=!0}),r.innerHTML="",t==="body"&&(r.__cells=[],r.__detailsCell=null),e.filter(l=>!l.hidden).forEach((l,h,p)=>{let _;if(t==="body"){l._cells||(l._cells=[]),_=l._cells.find(g=>g._vacant),_||(_=this._createCell("td",l),l._onCellKeyDown&&_.addEventListener("keydown",l._onCellKeyDown.bind(l)),l._cells.push(_)),_.setAttribute("part","cell body-cell"),_.__parentRow=r,r.__cells.push(_);const m=r===this.$.sizer;if((!l._bodyContentHidden||m)&&r.appendChild(_),m&&(l._sizerCell=_),h===p.length-1&&this.rowDetailsRenderer){this._detailsCells||(this._detailsCells=[]);const g=this._detailsCells.find(b=>b._vacant)||this._createCell("td");this._detailsCells.indexOf(g)===-1&&this._detailsCells.push(g),g._content.parentElement||s.appendChild(g._content),this._configureDetailsCell(g),r.appendChild(g),r.__detailsCell=g,this._a11ySetRowDetailsCell(r,g),g._vacant=!1}n||(l._cells=[...l._cells])}else{const m=t==="header"?"th":"td";i||l.localName==="vaadin-grid-column-group"?(_=l[`_${t}Cell`],_||(_=this._createCell(m),l._onCellKeyDown&&_.addEventListener("keydown",l._onCellKeyDown.bind(l))),_._column=l,r.appendChild(_),l[`_${t}Cell`]=_):(l._emptyCells||(l._emptyCells=[]),_=l._emptyCells.find(g=>g._vacant)||this._createCell(m),_._column=l,r.appendChild(_),l._emptyCells.indexOf(_)===-1&&l._emptyCells.push(_)),_.part.add("cell",`${t}-cell`)}_._content.parentElement||s.appendChild(_._content),_._vacant=!1,_._column=l}),t!=="body"&&this.__debounceUpdateHeaderFooterRowVisibility(r),this.appendChild(s),this._frozenCellsChanged(),this._updateFirstAndLastColumnForRow(r)}__debounceUpdateHeaderFooterRowVisibility(r){r.__debounceUpdateHeaderFooterRowVisibility=R.debounce(r.__debounceUpdateHeaderFooterRowVisibility,V,()=>this.__updateHeaderFooterRowVisibility(r))}__updateHeaderFooterRowVisibility(r){if(!r)return;const e=Array.from(r.children).filter(t=>{const i=t._column;if(i._emptyCells&&i._emptyCells.indexOf(t)>-1)return!1;if(r.parentElement===this.$.header){if(i.headerRenderer)return!0;if(i.header===null)return!1;if(i.path||i.header!==void 0)return!0}else if(i.footerRenderer)return!0;return!1});r.hidden!==!e.length&&(r.hidden=!e.length),this._resetKeyboardNavigation()}_updateScrollerItem(r,e){this._preventScrollerRotatingCellFocus(r,e),this._columnTree&&(this._updateRowOrderParts(r,e),this._a11yUpdateRowRowindex(r,e),this._getItem(e,r))}_columnTreeChanged(r){this._renderColumnTree(r),this.recalculateColumnWidths(),this.__updateColumnsBodyContentHidden()}_updateRowOrderParts(r,e=r.index){Z(r,{first:e===0,last:e===this._flatSize-1,odd:e%2!==0,even:e%2===0})}_updateRowStateParts(r,{expanded:e,selected:t,detailsOpened:i}){Z(r,{expanded:e,collapsed:this.__isRowExpandable(r),selected:t,"details-opened":i})}_renderColumnTree(r){for(z(this.$.items,e=>{this._updateRow(e,r[r.length-1],"body",!1,!0);const t=this.__getRowModel(e);this._updateRowOrderParts(e),this._updateRowStateParts(e,t),this._filterDragAndDrop(e,t)});this.$.header.children.length<r.length;){const e=document.createElement("tr");e.setAttribute("part","row"),e.setAttribute("role","row"),e.setAttribute("tabindex","-1"),this.$.header.appendChild(e);const t=document.createElement("tr");t.setAttribute("part","row"),t.setAttribute("role","row"),t.setAttribute("tabindex","-1"),this.$.footer.appendChild(t)}for(;this.$.header.children.length>r.length;)this.$.header.removeChild(this.$.header.firstElementChild),this.$.footer.removeChild(this.$.footer.firstElementChild);z(this.$.header,(e,t,i)=>{this._updateRow(e,r[t],"header",t===r.length-1);const n=U(e);M(n,"first-header-row-cell",t===0),M(n,"last-header-row-cell",t===i.length-1)}),z(this.$.footer,(e,t,i)=>{this._updateRow(e,r[r.length-1-t],"footer",t===0);const n=U(e);M(n,"first-footer-row-cell",t===0),M(n,"last-footer-row-cell",t===i.length-1)}),this._updateRow(this.$.sizer,r[r.length-1]),this._resizeHandler(),this._frozenCellsChanged(),this._updateFirstAndLastColumn(),this._resetKeyboardNavigation(),this._a11yUpdateHeaderRows(),this._a11yUpdateFooterRows(),this.generateCellClassNames(),this.generateCellPartNames(),this.__updateHeaderAndFooter()}_updateItem(r,e){r._item=e;const t=this.__getRowModel(r);this._toggleDetailsCell(r,t.detailsOpened),this._a11yUpdateRowLevel(r,t.level),this._a11yUpdateRowSelected(r,t.selected),this._updateRowStateParts(r,t),this._generateCellClassNames(r,t),this._generateCellPartNames(r,t),this._filterDragAndDrop(r,t),z(r,i=>{if(!(i._column&&!i._column.isConnected)&&i._renderer){const n=i._column||this;i._renderer.call(n,i._content,n,t)}}),this._updateDetailsCellHeight(r),this._a11yUpdateRowExpanded(r,t.expanded)}_resizeHandler(){this._updateDetailsCellHeights(),this.__updateHorizontalScrollPosition()}_onAnimationEnd(r){r.animationName.indexOf("vaadin-grid-appear")===0&&(r.stopPropagation(),this.__tryToRecalculateColumnWidthsIfPending(),requestAnimationFrame(()=>{this.__scrollToPendingIndexes()}))}__getRowModel(r){return{index:r.index,item:r._item,level:this._getIndexLevel(r.index),expanded:this._isExpanded(r._item),selected:this._isSelected(r._item),detailsOpened:!!this.rowDetailsRenderer&&this._isDetailsOpened(r._item)}}_showTooltip(r){const e=this._tooltipController.node;if(e&&e.isConnected){const t=r.target;if(!this.__isCellFullyVisible(t))return;this._tooltipController.setTarget(t),this._tooltipController.setContext(this.getEventContext(r)),e._stateController.open({focus:r.type==="focusin",hover:r.type==="mouseenter"})}}__isCellFullyVisible(r){if(r.hasAttribute("frozen")||r.hasAttribute("frozen-to-end"))return!0;let{left:e,right:t}=this.getBoundingClientRect();const i=[...r.parentNode.children].find(l=>l.hasAttribute("last-frozen"));if(i){const l=i.getBoundingClientRect();e=this.__isRTL?e:l.right,t=this.__isRTL?l.left:t}const n=[...r.parentNode.children].find(l=>l.hasAttribute("first-frozen-to-end"));if(n){const l=n.getBoundingClientRect();e=this.__isRTL?l.right:e,t=this.__isRTL?t:l.left}const s=r.getBoundingClientRect();return s.left>=e&&s.right<=t}_hideTooltip(r){const e=this._tooltipController&&this._tooltipController.node;e&&e._stateController.close(r)}requestContentUpdate(){this.__updateHeaderAndFooter(),this.__updateVisibleRows()}__updateHeaderAndFooter(){(this._columnTree||[]).forEach(r=>{r.forEach(e=>{e._renderHeaderAndFooter&&e._renderHeaderAndFooter()})})}__updateVisibleRows(r,e){this.__virtualizer&&this.__virtualizer.update(r,e)}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Mt=de`
  @keyframes vaadin-grid-appear {
    to {
      opacity: 1;
    }
  }

  :host {
    display: flex;
    flex-direction: column;
    animation: 1ms vaadin-grid-appear;
    height: 400px;
    flex: 1 1 auto;
    align-self: stretch;
    position: relative;
  }

  :host([hidden]) {
    display: none !important;
  }

  :host([disabled]) {
    pointer-events: none;
  }

  #scroller {
    display: flex;
    flex-direction: column;
    min-height: 100%;
    transform: translateY(0);
    width: auto;
    height: auto;
    position: absolute;
    inset: 0;
  }

  :host([all-rows-visible]) {
    height: auto;
    align-self: flex-start;
    flex-grow: 0;
    width: 100%;
  }

  :host([all-rows-visible]) #scroller {
    width: 100%;
    height: 100%;
    position: relative;
  }

  :host([all-rows-visible]) #items {
    min-height: 1px;
  }

  #table {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
    overflow: auto;
    position: relative;
    outline: none;
    /* Workaround for a Desktop Safari bug: new stacking context here prevents the scrollbar from getting hidden */
    z-index: 0;
  }

  #header,
  #footer {
    display: block;
    position: -webkit-sticky;
    position: sticky;
    left: 0;
    overflow: visible;
    width: 100%;
    z-index: 1;
  }

  #header {
    top: 0;
  }

  th {
    text-align: inherit;
  }

  /* Safari doesn't work with "inherit" */
  [safari] th {
    text-align: initial;
  }

  #footer {
    bottom: 0;
  }

  #items {
    flex-grow: 1;
    flex-shrink: 0;
    display: block;
    position: -webkit-sticky;
    position: sticky;
    width: 100%;
    left: 0;
    overflow: visible;
  }

  [part~='row'] {
    display: flex;
    width: 100%;
    box-sizing: border-box;
    margin: 0;
  }

  [part~='row'][loading] [part~='body-cell'] ::slotted(vaadin-grid-cell-content) {
    visibility: hidden;
  }

  [column-rendering='lazy'] [part~='body-cell']:not([frozen]):not([frozen-to-end]) {
    transform: translateX(var(--_grid-lazy-columns-start));
  }

  #items [part~='row'] {
    position: absolute;
  }

  #items [part~='row']:empty {
    height: 100%;
  }

  [part~='cell']:not([part~='details-cell']) {
    flex-shrink: 0;
    flex-grow: 1;
    box-sizing: border-box;
    display: flex;
    width: 100%;
    position: relative;
    align-items: center;
    padding: 0;
    white-space: nowrap;
  }

  [part~='cell'] > [tabindex] {
    display: flex;
    align-items: inherit;
    outline: none;
    position: absolute;
    inset: 0;
  }

  /* Switch the focusButtonMode wrapping element to "position: static" temporarily
     when measuring real width of the cells in the auto-width columns. */
  [measuring-auto-width] [part~='cell'] > [tabindex] {
    position: static;
  }

  [part~='details-cell'] {
    position: absolute;
    bottom: 0;
    width: 100%;
    box-sizing: border-box;
    padding: 0;
  }

  [part~='cell'] ::slotted(vaadin-grid-cell-content) {
    display: block;
    width: 100%;
    box-sizing: border-box;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  [hidden] {
    display: none !important;
  }

  [frozen],
  [frozen-to-end] {
    z-index: 2;
    will-change: transform;
  }

  [no-scrollbars][safari] #table,
  [no-scrollbars][firefox] #table {
    overflow: hidden;
  }

  /* Reordering styles */
  :host([reordering]) [part~='cell'] ::slotted(vaadin-grid-cell-content),
  :host([reordering]) [part~='resize-handle'],
  #scroller[no-content-pointer-events] [part~='cell'] ::slotted(vaadin-grid-cell-content) {
    pointer-events: none;
  }

  [part~='reorder-ghost'] {
    visibility: hidden;
    position: fixed;
    pointer-events: none;
    opacity: 0.5;

    /* Prevent overflowing the grid in Firefox */
    top: 0;
    left: 0;
  }

  :host([reordering]) {
    -moz-user-select: none;
    -webkit-user-select: none;
    user-select: none;
  }

  /* Resizing styles */
  [part~='resize-handle'] {
    position: absolute;
    top: 0;
    right: 0;
    height: 100%;
    cursor: col-resize;
    z-index: 1;
  }

  [part~='resize-handle']::before {
    position: absolute;
    content: '';
    height: 100%;
    width: 35px;
    transform: translateX(-50%);
  }

  [last-column] [part~='resize-handle']::before,
  [last-frozen] [part~='resize-handle']::before {
    width: 18px;
    transform: none;
    right: 0;
  }

  [frozen-to-end] [part~='resize-handle'] {
    left: 0;
    right: auto;
  }

  [frozen-to-end] [part~='resize-handle']::before {
    left: 0;
    right: auto;
  }

  [first-frozen-to-end] [part~='resize-handle']::before {
    width: 18px;
    transform: none;
  }

  [first-frozen-to-end] {
    margin-inline-start: auto;
  }

  /* Hide resize handle if scrolled to end */
  :host(:not([overflow~='end'])) [first-frozen-to-end] [part~='resize-handle'] {
    display: none;
  }

  #scroller[column-resizing] {
    -ms-user-select: none;
    -moz-user-select: none;
    -webkit-user-select: none;
    user-select: none;
  }

  /* Sizer styles */
  #sizer {
    display: flex;
    position: absolute;
    visibility: hidden;
  }

  #sizer [part~='details-cell'] {
    display: none !important;
  }

  #sizer [part~='cell'][hidden] {
    display: none !important;
  }

  #sizer [part~='cell'] {
    display: block;
    flex-shrink: 0;
    line-height: 0;
    height: 0 !important;
    min-height: 0 !important;
    max-height: 0 !important;
    padding: 0 !important;
    border: none !important;
  }

  #sizer [part~='cell']::before {
    content: '-';
  }

  #sizer [part~='cell'] ::slotted(vaadin-grid-cell-content) {
    display: none !important;
  }

  /* RTL specific styles */

  :host([dir='rtl']) #items,
  :host([dir='rtl']) #header,
  :host([dir='rtl']) #footer {
    left: auto;
  }

  :host([dir='rtl']) [part~='reorder-ghost'] {
    left: auto;
    right: 0;
  }

  :host([dir='rtl']) [part~='resize-handle'] {
    left: 0;
    right: auto;
  }

  :host([dir='rtl']) [part~='resize-handle']::before {
    transform: translateX(50%);
  }

  :host([dir='rtl']) [last-column] [part~='resize-handle']::before,
  :host([dir='rtl']) [last-frozen] [part~='resize-handle']::before {
    left: 0;
    right: auto;
  }

  :host([dir='rtl']) [frozen-to-end] [part~='resize-handle'] {
    right: 0;
    left: auto;
  }

  :host([dir='rtl']) [frozen-to-end] [part~='resize-handle']::before {
    right: 0;
    left: auto;
  }

  @media (forced-colors: active) {
    [part~='selected-row'] [part~='first-column-cell']::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      bottom: 0;
      border: 2px solid;
    }

    [part~='focused-cell']::before {
      outline: 2px solid !important;
      outline-offset: -1px;
    }
  }
`;/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */le("vaadin-grid",Mt,{moduleId:"vaadin-grid-styles"});class Q extends kt(rt(ke(nt(oe)))){static get template(){return Le`
      <div
        id="scroller"
        safari$="[[_safari]]"
        ios$="[[_ios]]"
        loading$="[[loading]]"
        column-reordering-allowed$="[[columnReorderingAllowed]]"
      >
        <table id="table" role="treegrid" aria-multiselectable="true" tabindex="0">
          <caption id="sizer" part="row"></caption>
          <thead id="header" role="rowgroup"></thead>
          <tbody id="items" role="rowgroup"></tbody>
          <tfoot id="footer" role="rowgroup"></tfoot>
        </table>

        <div part="reorder-ghost"></div>
      </div>

      <slot name="tooltip"></slot>

      <div id="focusexit" tabindex="0"></div>
    `}static get is(){return"vaadin-grid"}}ae(Q);le("vaadin-grid-sorter",de`
    :host {
      justify-content: flex-start;
      align-items: baseline;
      -webkit-user-select: none;
      -moz-user-select: none;
      user-select: none;
      cursor: var(--lumo-clickable-cursor);
    }

    [part='content'] {
      display: inline-block;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    [part='indicators'] {
      margin-left: var(--lumo-space-s);
    }

    [part='indicators']::before {
      transform: scale(0.8);
    }

    :host(:not([direction]):not(:hover)) [part='indicators'] {
      color: var(--lumo-tertiary-text-color);
    }

    :host([direction]) {
      color: var(--vaadin-selection-color-text, var(--lumo-primary-text-color));
    }

    [part='order'] {
      font-size: var(--lumo-font-size-xxs);
      line-height: 1;
    }

    /* RTL specific styles */

    :host([dir='rtl']) [part='indicators'] {
      margin-right: var(--lumo-space-s);
      margin-left: 0;
    }
  `,{moduleId:"lumo-grid-sorter"});/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Ue=document.createElement("template");Ue.innerHTML=`
  <style>
    @font-face {
      font-family: 'vaadin-grid-sorter-icons';
      src: url(data:application/font-woff;charset=utf-8;base64,d09GRgABAAAAAAQwAA0AAAAABuwAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAABGRlRNAAAEFAAAABkAAAAcfep+mUdERUYAAAP4AAAAHAAAAB4AJwAOT1MvMgAAAZgAAAA/AAAAYA8TBPpjbWFwAAAB7AAAAFUAAAFeF1fZ4mdhc3AAAAPwAAAACAAAAAgAAAAQZ2x5ZgAAAlgAAABcAAAAnMvguMloZWFkAAABMAAAAC8AAAA2C5Ap72hoZWEAAAFgAAAAHQAAACQGbQPHaG10eAAAAdgAAAAUAAAAHAoAAABsb2NhAAACRAAAABIAAAASAIwAYG1heHAAAAGAAAAAFgAAACAACwAKbmFtZQAAArQAAAECAAACZxWCgKhwb3N0AAADuAAAADUAAABZCrApUXicY2BkYGAA4rDECVrx/DZfGbhZGEDgyqNPOxH0/wNMq5kPALkcDEwgUQBWRA0dAHicY2BkYGA+8P8AAwMLAwgwrWZgZEAFbABY4QM8AAAAeJxjYGRgYOAAQiYGEICQSAAAAi8AFgAAeJxjYGY6yziBgZWBgWkm0xkGBoZ+CM34msGYkZMBFTAKoAkwODAwvmRiPvD/AIMDMxCD1CDJKjAwAgBktQsXAHicY2GAAMZQCM0EwqshbAALxAEKeJxjYGBgZoBgGQZGBhCIAPIYwXwWBhsgzcXAwcAEhIwMCi+Z/v/9/x+sSuElA4T9/4k4K1gHFwMMMILMY2QDYmaoABOQYGJABUA7WBiGNwAAJd4NIQAAAAAAAAAACAAIABAAGAAmAEAATgAAeJyNjLENgDAMBP9tIURJwQCMQccSZgk2i5fIYBDAidJjycXr7x5EPwE2wY8si7jmyBNXGo/bNBerxJNrpxhbO3/fEFpx8ZICpV+ghxJ74fAMe+h7Ox14AbrsHB14nK2QQWrDMBRER4mTkhQK3ZRQKOgCNk7oGQqhhEIX2WSlWEI1BAlkJ5CDdNsj5Ey9Rncdi38ES+jzNJo/HwTgATcoDEthhY3wBHc4CE+pfwsX5F/hGe7Vo/AcK/UhvMSz+mGXKhZU6pww8ISz3oWn1BvhgnwTnuEJf8Jz1OpFeIlX9YULDLdFi4ASHolkSR0iuYdjLak1vAequBhj21D61Nqyi6l3qWybGPjySbPHGScGJl6dP58MYcQRI0bts7mjebBqrFENH7t3qWtj0OuqHnXcW7b0HOTZFnKryRGW2hFX1m0O2vEM3opNMfTau+CS6Z3Vx6veNnEXY6jwDxhsc2gAAHicY2BiwA84GBgYmRiYGJkZmBlZGFkZ2djScyoLMgzZS/MyDQwMwLSrpYEBlIbxjQDrzgsuAAAAAAEAAf//AA94nGNgZGBg4AFiMSBmYmAEQnYgZgHzGAAD6wA2eJxjYGBgZACCKyoz1cD0o087YTQATOcIewAAAA==) format('woff');
      font-weight: normal;
      font-style: normal;
    }
  </style>
`;document.head.appendChild(Ue.content);le("vaadin-grid-sorter",de`
    :host {
      display: inline-flex;
      cursor: pointer;
      max-width: 100%;
    }

    [part='content'] {
      flex: 1 1 auto;
    }

    [part='indicators'] {
      position: relative;
      align-self: center;
      flex: none;
    }

    [part='order'] {
      display: inline;
      vertical-align: super;
    }

    [part='indicators']::before {
      font-family: 'vaadin-grid-sorter-icons';
      display: inline-block;
    }

    :host(:not([direction])) [part='indicators']::before {
      content: '\\e901';
    }

    :host([direction='asc']) [part='indicators']::before {
      content: '\\e900';
    }

    :host([direction='desc']) [part='indicators']::before {
      content: '\\e902';
    }
  `,{moduleId:"vaadin-grid-sorter-styles"});const Bt=o=>class extends o{static get properties(){return{path:String,direction:{type:String,reflectToAttribute:!0,notify:!0,value:null,sync:!0},_order:{type:Number,value:null,sync:!0}}}static get observers(){return["_pathOrDirectionChanged(path, direction)"]}ready(){super.ready(),this.addEventListener("click",this._onClick.bind(this))}connectedCallback(){super.connectedCallback(),this._grid?this._grid.__applySorters():this.__dispatchSorterChangedEvenIfPossible()}disconnectedCallback(){super.disconnectedCallback(),!this.parentNode&&this._grid?this._grid.__removeSorters([this]):this._grid&&this._grid.__applySorters()}_pathOrDirectionChanged(){this.__dispatchSorterChangedEvenIfPossible()}__dispatchSorterChangedEvenIfPossible(){this.path===void 0||this.direction===void 0||!this.isConnected||(this.dispatchEvent(new CustomEvent("sorter-changed",{detail:{shiftClick:!!this._shiftClick,fromSorterClick:!!this._fromSorterClick},bubbles:!0,composed:!0})),this._fromSorterClick=!1,this._shiftClick=!1)}_getDisplayOrder(e){return e===null?"":e+1}_onClick(e){if(e.defaultPrevented)return;const t=this.getRootNode().activeElement;this!==t&&this.contains(t)||(e.preventDefault(),this._shiftClick=e.shiftKey,this._fromSorterClick=!0,this.direction==="asc"?this.direction="desc":this.direction==="desc"?this.direction=null:this.direction="asc")}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */class Nt extends Bt(ke($e(oe))){static get template(){return Le`
      <div part="content">
        <slot></slot>
      </div>
      <div part="indicators">
        <span part="order">[[_getDisplayOrder(_order)]]</span>
      </div>
    `}static get is(){return"vaadin-grid-sorter"}}ae(Nt);(function(){const o=function(r){return window.Vaadin.Flow.tryCatchWrapper(r,"Vaadin Grid")};window.Vaadin.Flow.gridConnector={initLazy:r=>o(function(e){if(e.$connector)return;const t=e._dataProviderController;t.ensureFlatIndexHierarchyOriginal=t.ensureFlatIndexHierarchy,t.ensureFlatIndexHierarchy=o(function(a){const{item:c}=this.getFlatIndexContext(a);if(!c||!this.isExpanded(c))return;e.$connector.hasCacheForParentKey(e.getItemId(c))?this.ensureFlatIndexHierarchyOriginal(a):e.$connector.beforeEnsureFlatIndexHierarchy(a,c)}),t.isLoadingOriginal=t.isLoading,t.isLoading=o(function(){return e.$connector.hasEnsureSubCacheQueue()||this.isLoadingOriginal()}),t.getItemSubCache=o(function(a){return this.getItemContext(a)?.subCache});let i={};const n=50,s=20;let l=[],h,p=[],_;const m=150;let g,b={};const v="null";b[v]=[0,0];let S=null,D=null;const L=["SINGLE","NONE","MULTI"];let I={},P="SINGLE",O=!1;e.size=0,e.itemIdPath="key";function E(a){return{[e.itemIdPath]:a}}e.$connector={},e.$connector.hasCacheForParentKey=o(a=>i[a]?.size!==void 0),e.$connector.hasEnsureSubCacheQueue=o(()=>p.length>0),e.$connector.hasParentRequestQueue=o(()=>l.length>0),e.$connector.hasRootRequestQueue=o(()=>{const{pendingRequests:a}=t.rootCache;return Object.keys(a).length>0||!!g?.isActive()}),e.$connector.beforeEnsureFlatIndexHierarchy=o(function(a,c){p.push({flatIndex:a,itemkey:e.getItemId(c)}),_=_e.debounce(_,st,()=>{for(;p.length;)e.$connector.flushEnsureSubCache()})}),e.$connector.doSelection=o(function(a,c){P==="NONE"||!a.length||c&&e.hasAttribute("disabled")||(P==="SINGLE"&&(I={}),a.forEach(d=>{d&&(I[d.key]=d,d.selected=!0,c&&e.$server.select(d.key));const u=!e.activeItem||!d||d.key!=e.activeItem.key;!c&&P==="SINGLE"&&u&&(e.activeItem=d)}),e.selectedItems=Object.values(I))}),e.$connector.doDeselection=o(function(a,c){if(P==="NONE"||!a.length||c&&e.hasAttribute("disabled"))return;const d=e.selectedItems.slice();for(;a.length;){const u=a.shift();for(let f=0;f<d.length;f++){const C=d[f];if(u?.key===C.key){d.splice(f,1);break}}u&&(delete I[u.key],delete u.selected,c&&e.$server.deselect(u.key))}e.selectedItems=d}),e.__activeItemChanged=o(function(a,c){P=="SINGLE"&&(a?I[a.key]||e.$connector.doSelection([a],!0):c&&I[c.key]&&(e.__deselectDisallowed?e.activeItem=c:e.$connector.doDeselection([c],!0)))}),e._createPropertyObserver("activeItem","__activeItemChanged",!0),e.__activeItemChangedDetails=o(function(a,c){e.__disallowDetailsOnClick||a==null&&c===void 0||(a&&!a.detailsOpened?e.$server.setDetailsVisible(a.key):e.$server.setDetailsVisible(null))}),e._createPropertyObserver("activeItem","__activeItemChangedDetails",!0),e.$connector._getSameLevelPage=o(function(a,c,d){if((c.parentItem?e.getItemId(c.parentItem):v)===a)return Math.floor(d/e.pageSize);const{parentCache:f,parentCacheIndex:C}=c;return f?this._getSameLevelPage(a,f,C):null}),e.$connector.flushEnsureSubCache=o(function(){const a=p.shift();return a?(t.ensureFlatIndexHierarchyOriginal(a.flatIndex),!0):!1}),e.$connector.debounceRootRequest=o(function(a){const c=e._hasData?m:0;g=_e.debounce(g,Ie.after(c),()=>{e.$connector.fetchPage((d,u)=>e.$server.setRequestedRange(d,u),a,v)})}),e.$connector.flushParentRequests=o(function(){const a=[];l.splice(0,s).forEach(({parentKey:c,page:d})=>{e.$connector.fetchPage((u,f)=>a.push({parentKey:c,firstIndex:u,size:f}),d,c)}),a.length&&e.$server.setParentRequestedRanges(a)}),e.$connector.debounceParentRequest=o(function(a,c){l=l.filter(d=>d.parentKey!==a),l.push({parentKey:a,page:c}),h=_e.debounce(h,Ie.after(n),()=>{for(;l.length;)e.$connector.flushParentRequests()})}),e.$connector.fetchPage=o(function(a,c,d){d===v&&(c=Math.min(c,Math.floor((e.size-1)/e.pageSize)));const u=e._getRenderedRows();let f=u.length>0?u[0].index:0,C=u.length>0?u[u.length-1].index:0,y=C-f,x=Math.max(0,f-y),w=Math.min(C+y,e._flatSize),A=[null,null];for(let T=x;T<=w;T++){const{cache:Ke,index:je}=t.getFlatIndexContext(T),j=e.$connector._getSameLevelPage(d,Ke,je);j!==null&&(A[0]=Math.min(A[0]??j,j),A[1]=Math.max(A[1]??j,j))}(A.some(T=>T===null)||c<A[0]||c>A[1])&&(A=[c,c]);let F=b[d]||[-1,-1];if(F[0]!=A[0]||F[1]!=A[1]){b[d]=A;let T=A[1]-A[0]+1;a(A[0]*e.pageSize,T*e.pageSize)}}),e.dataProvider=o(function(a,c){if(a.pageSize!=e.pageSize)throw"Invalid pageSize";let d=a.page;if(a.parentItem){let u=e.getItemId(a.parentItem);const f=t.getItemSubCache(a.parentItem);i[u]?.[d]&&f?(p=[],c(i[u][d],i[u].size)):e.$connector.debounceParentRequest(u,d)}else{if(e.size===0){c([],0);return}i[v]?.[d]?c(i[v][d]):e.$connector.debounceRootRequest(d)}}),e.$connector.setSorterDirections=o(function(a){O=!0,setTimeout(o(()=>{try{const c=Array.from(e.querySelectorAll("vaadin-grid-sorter"));e._sorters.forEach(d=>{c.includes(d)||c.push(d)}),c.forEach(d=>{d.direction=null}),e.multiSortPriority!=="append"&&(a=a.reverse()),a.forEach(({column:d,direction:u})=>{c.forEach(f=>{f.getAttribute("path")===d&&(f.direction=u)})}),e.__applySorters()}finally{O=!1}}))}),e._updateItem=o(function(a,c){Q.prototype._updateItem.call(e,a,c),a.hidden||Array.from(a.children).forEach(d=>{Array.from(d?._content?.__templateInstance?.children||[]).forEach(u=>{u._attachRenderedComponentIfAble&&u._attachRenderedComponentIfAble(),Array.from(u?.children||[]).forEach(f=>{f._attachRenderedComponentIfAble&&f._attachRenderedComponentIfAble()})})}),P===L[1]&&(a.removeAttribute("aria-selected"),Array.from(a.children).forEach(d=>d.removeAttribute("aria-selected")))});const $=o(function(a,c){if(a==null||e.$server.updateExpandedState==null)return;let d=e.getItemId(a);e.$server.updateExpandedState(d,c)});e.expandItem=o(function(a){$(a,!0),Q.prototype.expandItem.call(e,a)}),e.collapseItem=o(function(a){$(a,!1),Q.prototype.collapseItem.call(e,a)});const K=function(a){if(!a||!Array.isArray(a))throw"Attempted to call itemsUpdated with an invalid value: "+JSON.stringify(a);let c=Array.from(e.detailsOpenedItems),d=!1;for(let u=0;u<a.length;++u){const f=a[u];f&&(f.detailsOpened?e._getItemIndexInArray(f,c)<0&&c.push(f):e._getItemIndexInArray(f,c)>=0&&c.splice(e._getItemIndexInArray(f,c),1),I[f.key]&&(I[f.key]=f,f.selected=!0,d=!0))}e.detailsOpenedItems=c,d&&e.selectedItems.splice(0,e.selectedItems.length,...Object.values(I))},he=function(a,c=v){const d=i[c][a],u=E(c);let f=c===v?t.rootCache:t.getItemSubCache(u);return f&&!f.pendingRequests[a]&&f.setPage(a,d||Array.from({length:e.pageSize})),d},Ce=function(){ue(),e.__updateVisibleRows()},ue=function(){t.recalculateFlatSize(),e._flatSize=t.flatSize},J=function(a){if(!a||!e.$||e.$.items.childElementCount===0)return;const c=a.map(u=>u.key),d=e._getRenderedRows().filter(u=>u._item&&c.includes(u._item.key)).map(u=>u.index);d.length>0&&e.__updateVisibleRows(d[0],d[d.length-1])};e.$connector.set=o(function(a,c,d){if(a%e.pageSize!=0)throw"Got new data to index "+a+" which is not aligned with the page size of "+e.pageSize;let u=d||v;const f=a/e.pageSize,C=Math.ceil(c.length/e.pageSize);u===v&&(D=[f,f+C-1]);for(let y=0;y<C;y++){let x=f+y,w=c.slice(y*e.pageSize,(y+1)*e.pageSize);i[u]||(i[u]={}),i[u][x]=w,e.$connector.doSelection(w.filter(F=>F.selected)),e.$connector.doDeselection(w.filter(F=>!F.selected&&I[F.key]));const A=he(x,u);A&&(K(A),J(A))}});const be=function(a){let c=a.parentUniqueKey||v;if(i[c]){for(let d in i[c])for(let u in i[c][d])if(e.getItemId(i[c][d][u])===e.getItemId(a))return{page:d,index:u,parentKey:c}}return null};e.$connector.updateHierarchicalData=o(function(a){let c=[];for(let u=0;u<a.length;u++){let f=be(a[u]);if(f){i[f.parentKey][f.page][f.index]=a[u];let C=f.parentKey+":"+f.page;c[C]||(c[C]={parentKey:f.parentKey,page:f.page})}}let d=Object.keys(c);for(let u=0;u<d.length;u++){let f=c[d[u]];const C=he(f.page,f.parentKey);C&&(K(C),J(C))}}),e.$connector.updateFlatData=o(function(a){for(let c=0;c<a.length;c++){let d=be(a[c]);if(d){i[d.parentKey][d.page][d.index]=a[c];const u=parseInt(d.page)*e.pageSize+parseInt(d.index),{rootCache:f}=t;f.items[u]&&(f.items[u]=a[c])}}K(a),J(a)}),e.$connector.clearExpanded=o(function(){e.expandedItems=[],p=[],l=[]});const qe=function(){const a=b[v];if(!a||!S)return;const c=S[1]-S[0]+1,d=Array.from({length:c},(u,f)=>S[0]+f);if(D){const[u,f]=D;for(let C=u;C<=f;C++){const y=d.indexOf(C);y>=0&&d.splice(y,1)}}d.some(u=>u>=a[0]&&u<=a[1])&&(a[0]=-1,a[1]=-1)};e.$connector.clear=o(function(a,c,d){let u=d||v;if(!i[u]||Object.keys(i[u]).length===0)return;if(a%e.pageSize!=0)throw"Got cleared data for index "+a+" which is not aligned with the page size of "+e.pageSize;let f=Math.floor(a/e.pageSize),C=Math.ceil(c/e.pageSize);u===v&&(S=[f,f+C-1]);for(let w=0;w<C;w++){let A=f+w,F=i[u][A];e.$connector.doDeselection(F.filter(T=>I[T.key])),F.forEach(T=>e.closeItemDetails(T)),delete i[u][A],he(A,d),J(F)}let y=t.rootCache;if(d){const w=E(u);y=t.getItemSubCache(w)}const x=a+C*e.pageSize;for(let w=a;w<x;w++)delete y.items[w],y.removeSubCache(w);ue()}),e.$connector.reset=o(function(){e.size=0,i={},t.rootCache.items=[],b={},_&&_.cancel(),h&&h.cancel(),g&&g.cancel(),_=void 0,h=void 0,p=[],l=[],Ce()}),e.$connector.updateSize=a=>e.size=a,e.$connector.updateUniqueItemIdPath=a=>e.itemIdPath=a,e.$connector.expandItems=o(function(a){let c=Array.from(e.expandedItems);a.filter(d=>!e._isExpanded(d)).forEach(d=>c.push(d)),e.expandedItems=c}),e.$connector.collapseItems=o(function(a){let c=Array.from(e.expandedItems);a.forEach(d=>{let u=e._getItemIndexInArray(d,c);u>=0&&c.splice(u,1)}),e.expandedItems=c,a.forEach(d=>e.$connector.removeFromQueue(d))}),e.$connector.removeFromQueue=o(function(a){const c=t.getItemSubCache(a);Object.values(c?.pendingRequests||{}).forEach(u=>u([]));const d=e.getItemId(a);p=p.filter(u=>u.itemkey!==d),l=l.filter(u=>u.parentKey!==d)}),e.$connector.confirmParent=o(function(a,c,d){i[c]||(i[c]={});const u=i[c].size!==d;i[c].size=d,d===0&&(i[c][0]=[]);const f=E(c),C=t.getItemSubCache(f);if(C){const{pendingRequests:y}=C;Object.entries(y).forEach(([x,w])=>{let A=b[c]||[0,0];if(i[c]&&i[c][x]||x<A[0]||x>A[1]){let F=i[c][x]||new Array(d);w(F,d)}else w&&d===0&&w([],d)}),u&&Object.keys(y).length===0&&(C.size=d,ue())}e.$server.confirmParentUpdate(a,c)}),e.$connector.confirm=o(function(a){const{pendingRequests:c}=t.rootCache;Object.entries(c).forEach(([d,u])=>{const f=b[v]||[0,0],C=e.size?Math.ceil(e.size/e.pageSize)-1:0,y=Math.min(f[1],C);i[v]?.[d]?u(i[v][d]):d<f[0]||+d>y?(u(new Array(e.pageSize)),e.requestContentUpdate()):u&&e.size===0&&u([])}),qe(),D=null,S=null,e.$server.confirmUpdate(a)}),e.$connector.ensureHierarchy=o(function(){for(let a in i)a!==v&&delete i[a];b={},t.rootCache.removeSubCaches(),Ce()}),e.$connector.setSelectionMode=o(function(a){if((typeof a=="string"||a instanceof String)&&L.indexOf(a)>=0)P=a,I={},e.$connector.updateMultiSelectable();else throw"Attempted to set an invalid selection mode"}),e.$connector.updateMultiSelectable=o(function(){e.$&&(P===L[0]?e.$.table.setAttribute("aria-multiselectable",!1):P===L[1]?e.$.table.removeAttribute("aria-multiselectable"):e.$.table.setAttribute("aria-multiselectable",!0))}),e._createPropertyObserver("isAttached",()=>e.$connector.updateMultiSelectable());const ve=a=>c=>{a&&(a(c),a=null)};e.$connector.setHeaderRenderer=o(function(a,c){const{content:d,showSorter:u,sorterPath:f}=c;if(d===null){a.headerRenderer=null;return}a.headerRenderer=ve(C=>{C.innerHTML="";let y=C;if(u){const x=document.createElement("vaadin-grid-sorter");x.setAttribute("path",f);const w=d instanceof Node?d.textContent:d;w&&x.setAttribute("aria-label",`Sort by ${w}`),C.appendChild(x),y=x}d instanceof Node?y.appendChild(d):y.textContent=d})}),e._getActiveSorters=function(){return this._sorters.filter(a=>a.direction)},e.__applySorters=()=>{const a=e._mapSorters(),c=JSON.stringify(e._previousSorters)!==JSON.stringify(a);e._previousSorters=a,Q.prototype.__applySorters.call(e),c&&!O&&e.$server.sortersChanged(a)},e.$connector.setFooterRenderer=o(function(a,c){const{content:d}=c;if(d===null){a.footerRenderer=null;return}a.footerRenderer=ve(u=>{u.innerHTML="",d instanceof Node?u.appendChild(d):u.textContent=d})}),e.addEventListener("vaadin-context-menu-before-open",o(function(a){const{key:c,columnId:d}=a.detail;e.$server.updateContextMenuTargetItem(c,d)})),e.getContextMenuBeforeOpenDetail=o(function(a){const c=a.detail.sourceEvent||a,d=e.getEventContext(c),u=d.item?.key||"",f=d.column?.id||"";return{key:u,columnId:f}}),e.preventContextMenu=o(function(a){const c=a.type==="click",{column:d}=e.getEventContext(a);return c&&d instanceof fe}),e.addEventListener("click",o(a=>ye(a,"item-click"))),e.addEventListener("dblclick",o(a=>ye(a,"item-double-click"))),e.addEventListener("column-resize",o(a=>{e._getColumnsInOrder().filter(d=>!d.hidden).forEach(d=>{d.dispatchEvent(new CustomEvent("column-drag-resize"))}),e.dispatchEvent(new CustomEvent("column-drag-resize",{detail:{resizedColumnKey:a.detail.resizedColumn._flowId}}))})),e.addEventListener("column-reorder",o(a=>{const c=e._columnTree.slice(0).pop().filter(d=>d._flowId).sort((d,u)=>d._order-u._order).map(d=>d._flowId);e.dispatchEvent(new CustomEvent("column-reorder-all-columns",{detail:{columns:c}}))})),e.addEventListener("cell-focus",o(a=>{const c=e.getEventContext(a);["header","body","footer"].indexOf(c.section)!==-1&&e.dispatchEvent(new CustomEvent("grid-cell-focus",{detail:{itemKey:c.item?c.item.key:null,internalColumnId:c.column?c.column._flowId:null,section:c.section}}))}));function ye(a,c){if(a.defaultPrevented)return;const d=a.composedPath(),u=d.findIndex(x=>x.localName==="td"||x.localName==="th");if(d.slice(0,u).some(x=>He(x)||x instanceof HTMLLabelElement))return;const C=e.getEventContext(a),y=C.section;C.item&&y!=="details"&&(a.itemKey=C.item.key,C.column&&(a.internalColumnId=C.column._flowId),e.dispatchEvent(new CustomEvent(c,{detail:a})))}e.cellClassNameGenerator=o(function(a,c){const d=c.item.style;if(d)return(d.row||"")+" "+(a&&d[a._flowId]||"")}),e.cellPartNameGenerator=o(function(a,c){const d=c.item.part;if(d)return(d.row||"")+" "+(a&&d[a._flowId]||"")}),e.dropFilter=o(a=>a.item&&!a.item.dropDisabled),e.dragFilter=o(a=>a.item&&!a.item.dragDisabled),e.addEventListener("grid-dragstart",o(a=>{e._isSelected(a.detail.draggedItems[0])?(e.__selectionDragData?Object.keys(e.__selectionDragData).forEach(c=>{a.detail.setDragData(c,e.__selectionDragData[c])}):(e.__dragDataTypes||[]).forEach(c=>{a.detail.setDragData(c,a.detail.draggedItems.map(d=>d.dragData[c]).join(`
`))}),e.__selectionDraggedItemsCount>1&&a.detail.setDraggedItemsCount(e.__selectionDraggedItemsCount)):(e.__dragDataTypes||[]).forEach(c=>{a.detail.setDragData(c,a.detail.draggedItems[0].dragData[c])})}))})(r)}})();/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */const Ht=o=>class extends Be(o){static get properties(){return{_childColumns:{value(){return this._getChildColumns(this)}},flexGrow:{type:Number,readOnly:!0,sync:!0},width:{type:String,readOnly:!0},_visibleChildColumns:Array,_colSpan:Number,_rootColumns:Array}}static get observers(){return["_groupFrozenChanged(frozen, _rootColumns)","_groupFrozenToEndChanged(frozenToEnd, _rootColumns)","_groupHiddenChanged(hidden)","_colSpanChanged(_colSpan, _headerCell, _footerCell)","_groupOrderChanged(_order, _rootColumns)","_groupReorderStatusChanged(_reorderStatus, _rootColumns)","_groupResizableChanged(resizable, _rootColumns)"]}connectedCallback(){super.connectedCallback(),this._addNodeObserver(),this._updateFlexAndWidth()}disconnectedCallback(){super.disconnectedCallback(),this._observer&&this._observer.disconnect()}_columnPropChanged(r,e){r==="hidden"&&(this._preventHiddenSynchronization=!0,this._updateVisibleChildColumns(this._childColumns),this._preventHiddenSynchronization=!1),/flexGrow|width|hidden|_childColumns/u.test(r)&&this._updateFlexAndWidth(),r==="frozen"&&!this.frozen&&(this.frozen=e),r==="lastFrozen"&&!this._lastFrozen&&(this._lastFrozen=e),r==="frozenToEnd"&&!this.frozenToEnd&&(this.frozenToEnd=e),r==="firstFrozenToEnd"&&!this._firstFrozenToEnd&&(this._firstFrozenToEnd=e)}_groupOrderChanged(r,e){if(e){const t=e.slice(0);if(!r){t.forEach(l=>{l._order=0});return}const i=/(0+)$/u.exec(r).pop().length,n=~~(Math.log(e.length)/Math.LN10)+1,s=10**(i-n);t[0]&&t[0]._order&&t.sort((l,h)=>l._order-h._order),Me(t,s,r)}}_groupReorderStatusChanged(r,e){r===void 0||e===void 0||e.forEach(t=>{t._reorderStatus=r})}_groupResizableChanged(r,e){r===void 0||e===void 0||e.forEach(t=>{t.resizable=r})}_updateVisibleChildColumns(r){this._visibleChildColumns=Array.prototype.filter.call(r,e=>!e.hidden),this._colSpan=this._visibleChildColumns.length,this._updateAutoHidden()}_updateFlexAndWidth(){if(this._visibleChildColumns){if(this._visibleChildColumns.length>0){const r=this._visibleChildColumns.reduce((e,t)=>(e+=` + ${(t.width||"0px").replace("calc","")}`,e),"").substring(3);this._setWidth(`calc(${r})`)}else this._setWidth("0px");this._setFlexGrow(Array.prototype.reduce.call(this._visibleChildColumns,(r,e)=>r+e.flexGrow,0))}}__scheduleAutoFreezeWarning(r,e){if(this._grid){const t=e.replace(/([A-Z])/gu,"-$1").toLowerCase(),i=r[0][e]||r[0].hasAttribute(t);r.every(s=>(s[e]||s.hasAttribute(t))===i)||(this._grid.__autoFreezeWarningDebouncer=R.debounce(this._grid.__autoFreezeWarningDebouncer,se,()=>{console.warn(`WARNING: Joining ${e} and non-${e} Grid columns inside the same column group! This will automatically freeze all the joined columns to avoid rendering issues. If this was intentional, consider marking each joined column explicitly as ${e}. Otherwise, exclude the ${e} columns from the joined group.`)}))}}_groupFrozenChanged(r,e){e===void 0||r===void 0||r!==!1&&(this.__scheduleAutoFreezeWarning(e,"frozen"),Array.from(e).forEach(t=>{t.frozen=r}))}_groupFrozenToEndChanged(r,e){e===void 0||r===void 0||r!==!1&&(this.__scheduleAutoFreezeWarning(e,"frozenToEnd"),Array.from(e).forEach(t=>{t.frozenToEnd=r}))}_groupHiddenChanged(r){(r||this.__groupHiddenInitialized)&&this._synchronizeHidden(),this.__groupHiddenInitialized=!0}_updateAutoHidden(){const r=this._autoHidden;this._autoHidden=(this._visibleChildColumns||[]).length===0,(r||this._autoHidden)&&(this.hidden=this._autoHidden)}_synchronizeHidden(){this._childColumns&&!this._preventHiddenSynchronization&&this._childColumns.forEach(r=>{r.hidden=this.hidden})}_colSpanChanged(r,e,t){e&&(e.setAttribute("colspan",r),this._grid&&this._grid._a11yUpdateCellColspan(e,r)),t&&(t.setAttribute("colspan",r),this._grid&&this._grid._a11yUpdateCellColspan(t,r))}_getChildColumns(r){return B.getColumns(r)}_addNodeObserver(){this._observer=new B(this,()=>{this._preventHiddenSynchronization=!0,this._rootColumns=this._getChildColumns(this),this._childColumns=this._rootColumns,this._updateVisibleChildColumns(this._childColumns),this._preventHiddenSynchronization=!1,this._grid&&this._grid._debounceUpdateColumnTree&&this._grid._debounceUpdateColumnTree()}),this._observer.flush()}_isColumnElement(r){return r.nodeType===Node.ELEMENT_NODE&&/\bcolumn\b/u.test(r.localName)}};/**
 * @license
 * Copyright (c) 2016 - 2024 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */class Gt extends Ht(oe){static get is(){return"vaadin-grid-column-group"}}ae(Gt);/**
 * @license
 * Copyright 2020 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const Wt=dt(class extends ct{constructor(o){if(super(o),o.type!==G.PROPERTY&&o.type!==G.ATTRIBUTE&&o.type!==G.BOOLEAN_ATTRIBUTE)throw Error("The `live` directive is not allowed on child or event bindings");if(!ot(o))throw Error("`live` bindings can only contain a single expression")}render(o){return o}update(o,[r]){if(r===ee||r===ht)return r;const e=o.element,t=o.name;if(o.type===G.PROPERTY){if(r===e[t])return ee}else if(o.type===G.BOOLEAN_ATTRIBUTE){if(!!r===e.hasAttribute(t))return ee}else if(o.type===G.ATTRIBUTE&&e.getAttribute(t)===r+"")return ee;return at(o),r}}),ne=window;ne.Vaadin=ne.Vaadin||{};ne.Vaadin.setLitRenderer=(o,r,e,t,i,n)=>{const s=Function(`
    "use strict";

    const [render, html, live, returnChannel] = arguments;

    return (root, model, itemKey) => {
      const { item, index } = model;
      ${i.map(h=>`
          const ${h} = (...args) => {
            if (itemKey !== undefined) {
              returnChannel('${h}', itemKey, args[0] instanceof Event ? [] : [...args]);
            }
          }`).join("")}

      render(html\`${e}\`, root)
    }
  `)(ut,_t,Wt,t),l=(h,p,_)=>{const{item:m}=_;h.__litRenderer!==l&&(h.innerHTML="",delete h._$litPart$,h.__litRenderer=l);const g={};for(const b in m)b.startsWith(n)&&(g[b.replace(n,"")]=m[b]);s(h,{..._,item:g},m.key)};l.__rendererId=n,o[r]=l};ne.Vaadin.unsetLitRenderer=(o,r,e)=>{o[r]?.__rendererId===e&&(o[r]=void 0)};
